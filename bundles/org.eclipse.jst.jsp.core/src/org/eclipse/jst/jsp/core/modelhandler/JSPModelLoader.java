/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jsp.core.modelhandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jst.jsp.core.PageDirectiveAdapter;
import org.eclipse.jst.jsp.core.document.PageDirectiveAdapterFactory;
import org.eclipse.jst.jsp.core.document.PageDirectiveWatcherFactory;
import org.eclipse.jst.jsp.core.encoding.IJSPHeadContentDetector;
import org.eclipse.jst.jsp.core.encoding.JSPDocumentHeadContentDetector;
import org.eclipse.jst.jsp.core.encoding.JSPDocumentLoader;
import org.eclipse.jst.jsp.core.internal.parser.JSPReParser;
import org.eclipse.jst.jsp.core.internal.parser.JSPSourceParser;
import org.eclipse.jst.jsp.core.internal.text.rules.StructuredTextPartitionerForJSP;
import org.eclipse.jst.jsp.core.modelquery.ModelQueryAdapterFactoryForJSP;
import org.eclipse.wst.common.encoding.content.IContentTypeIdentifier;
import org.eclipse.wst.html.core.document.XMLStyleModelImpl;
import org.eclipse.wst.html.core.internal.text.rules.StructuredTextPartitionerForHTML;
import org.eclipse.wst.sse.core.AbstractModelLoader;
import org.eclipse.wst.sse.core.AdapterFactory;
import org.eclipse.wst.sse.core.INodeNotifier;
import org.eclipse.wst.sse.core.IStructuredModel;
import org.eclipse.wst.sse.core.ModelLoader;
import org.eclipse.wst.sse.core.PropagatingAdapter;
import org.eclipse.wst.sse.core.document.IDocumentLoader;
import org.eclipse.wst.sse.core.document.StructuredDocumentFactory;
import org.eclipse.wst.sse.core.internal.modelhandler.EmbeddedTypeRegistry;
import org.eclipse.wst.sse.core.internal.modelhandler.EmbeddedTypeRegistryImpl;
import org.eclipse.wst.sse.core.internal.text.BasicStructuredDocument;
import org.eclipse.wst.sse.core.modelhandler.EmbeddedTypeHandler;
import org.eclipse.wst.sse.core.modelquery.ModelQueryAdapter;
import org.eclipse.wst.sse.core.parser.JSPCapableParser;
import org.eclipse.wst.sse.core.parser.RegionParser;
import org.eclipse.wst.sse.core.text.IStructuredDocument;
import org.eclipse.wst.sse.core.util.Assert;
import org.eclipse.wst.sse.core.util.Debug;
import org.eclipse.wst.xml.core.document.XMLDocument;
import org.eclipse.wst.xml.core.document.XMLModel;
import org.eclipse.wst.xml.core.internal.DebugAdapterFactory;
import org.eclipse.wst.xml.core.internal.propagate.PropagatingAdapterFactoryImpl;
import org.eclipse.wst.xml.core.text.rules.StructuredTextPartitionerForXML;
import org.w3c.dom.Document;

public class JSPModelLoader extends AbstractModelLoader {
	protected final int MAX_BUFFERED_SIZE_FOR_RESET_MARK = 200000;

	/**
	 * DMW - Note: I think the embeddedTypeRegistry in IModelManager can be
	 * removed
	 */
	private EmbeddedTypeRegistry embeddedContentTypeRegistry;
	private final static String DEFAULT_MIME_TYPE = "text/html"; //$NON-NLS-1$
	private final static String SPEC_DEFAULT_ENCODING = "ISO-8859-1"; //$NON-NLS-1$
	private final static String DEFAULT_LANGUAGE = "java"; //$NON-NLS-1$

	public JSPModelLoader() {
		super();
	}

	/**
	 * Gets the embeddedContentTypeRegistry.
	 * 
	 * @return Returns a EmbeddedContentTypeRegistry
	 */
	private EmbeddedTypeRegistry getEmbeddedContentTypeRegistry() {
		if (embeddedContentTypeRegistry == null) {
			embeddedContentTypeRegistry = EmbeddedTypeRegistryImpl.getInstance();
		}
		return embeddedContentTypeRegistry;
	}

	public IStructuredModel newModel() {
		// future_TODO: this is similar to "hard coding"
		// that HTML will be in JSP file ... should make more
		// flexible in future.
		XMLStyleModelImpl model = new XMLStyleModelImpl();
		return model;
	}

	/**
	 * Specification cites ISO-8859-1/Latin-1 as the default charset.
	 */
	protected String getDefaultEncoding() {
		return SPEC_DEFAULT_ENCODING;
	}

	/**
	 * For JSP files, text/html is the default content type. This may want
	 * this different for types like jsv (jsp for voice xml) For now, hard
	 * code to new instance. In future, should get instance from registry.
	 * 
	 * Specification cites HTML as the default contentType.
	 */
	private EmbeddedTypeHandler getJSPDefaultEmbeddedType() {
		EmbeddedTypeRegistry reg = getEmbeddedContentTypeRegistry();
		return reg.getTypeFor(getDefaultMimeType());
	}

	/**
	 * Method getDefaultMimeType.
	 * 
	 * @return String
	 */
	private String getDefaultMimeType() {
		return DEFAULT_MIME_TYPE;
	}

	/**
	 * This method should retrieve the model from the file system (or what
	 * ever the loader is prepared to do). If the resource can not be found
	 * (and therefore a model can not be created), then it should return null.
	 */
	//	public void load(Reader reader, IStructuredModel model, EncodingRule
	// encodingRule) throws java.io.IOException {
	//		initializeEmbeddedTypeFromStream(reader, model);
	//		setLanguageInPageDirective(model);
	//		super.load(reader, model, encodingRule);
	//	}
	/**
	 * This method must return a new instance of IStructuredDocument, that has
	 * been initialized with appropriate parser. For many loaders, the
	 * (default) parser used is known for any input. For others, the correct
	 * parser (and its initialization) is normall dependent on the content of
	 * the file. This no-argument method should assume "empty input" and would
	 * therefore return the default parser for the default contentType.
	 * 
	 * If the parser is to handle tag libraries, it must have a TaglibSupport
	 * object with a valid URIResolver and this IStructuredDocument attached
	 * to it before the contents are set on the IStructuredDocument.
	 */
	public IStructuredDocument newStructuredDocument() {
		IStructuredDocument structuredDocument = StructuredDocumentFactory.getNewStructuredDocumentInstance(getParser());
		((BasicStructuredDocument) structuredDocument).setReParser(new JSPReParser());
		//		structuredDocument.setDocumentPartitioner(new
		// JSPJavaDocumentPartioner());
		// even though this is an "empty model" ... we want it to have at
		// least the
		// default embeddeded content type handler
		EmbeddedTypeHandler embeddedType = getJSPDefaultEmbeddedType();
		embeddedType.initializeParser((JSPCapableParser) structuredDocument.getParser());
		return structuredDocument;
	}

	public RegionParser getParser() {
		// remember, the Loader
		// will need to finish initialization of parser
		// based on "embedded content"
		return new JSPSourceParser();
	}

	protected void preLoadAdapt(IStructuredModel structuredModel) {
		super.preLoadAdapt(structuredModel);
		XMLModel domModel = (XMLModel) structuredModel;
		//
		// document must have already been set for this to
		// work.
		Document document = domModel.getDocument();
		Assert.isNotNull(document);
		// if there is a model in the adapter, this will adapt it to
		// first node. After that the PropagatingAdater spreads over the
		// children being
		// created. Each time that happends, a side effect is to
		// also "spread" sprecific registered adapters,
		// they two can propigate is needed.
		// This 'get' causes first to be be attached.
		PropagatingAdapter propagatingAdapter = (PropagatingAdapter) ((INodeNotifier) document).getAdapterFor(PropagatingAdapter.class);
		// may make this easier to use in futue
		propagatingAdapter.addAdaptOnCreateFactory(new PageDirectiveWatcherFactory());
		if (Debug.debugNotificationAndEvents) {
			propagatingAdapter.addAdaptOnCreateFactory(new DebugAdapterFactory());
		}
		// For JSPs, the ModelQueryAdapter must be "attached" to the document
		// before content is set in the model, so taglib initization can
		// take place.
		((INodeNotifier) document).getAdapterFor(ModelQueryAdapter.class);
		//

	}

	/**
	 * This method must return those factories which must be attached to the
	 * structuredModel before content is applied.
	 */
	public List getAdapterFactories() {
		List result = new ArrayList();
		AdapterFactory factory = null;
		//
		factory = new PropagatingAdapterFactoryImpl();
		result.add(factory);
		factory = new PageDirectiveAdapterFactory();
		result.add(factory);



		return result;
	}

	/**
	 * @see com.ibm.sed.model.AbstractDumper#getDocumentEncodingDetector()
	 */
	public IJSPHeadContentDetector getHeadParser() {
		return new JSPDocumentHeadContentDetector();
	}

	/**
	 * This init method is for the case where we are getting the embedded type
	 * from the input stream specifically.
	 */
	//	private void initializeEmbeddedTypeFromStream(Reader reader,
	// IStructuredModel model) throws IOException {
	//		IFile file = ResourceUtil.getFileFor(model);
	//		JSPDocumentLoader jspDocumentLoader =(JSPDocumentLoader)
	// getDocumentLoader();
	//		EmbeddedTypeHandler inputEmbeddedContentType =
	// jspDocumentLoader.getEmbeddedType(file);
	//		EmbeddedTypeHandler existingEmbeddedType = getEmbeddedType(model);
	//		// we don't expect the existing type to be null, but if it is, then
	//		// this is a simply init case, not re-init
	//		if (existingEmbeddedType == null) {
	//			initializeEmbeddedType(model, inputEmbeddedContentType);
	//		}
	//		else {
	//			if (existingEmbeddedType != inputEmbeddedContentType) {
	//				// only need to reinitialize if truely different
	//				reInitializeEmbeddedType(model, existingEmbeddedType,
	// inputEmbeddedContentType);
	//			}
	//		}
	//	}
	/**
	 * This init method is for the case where we are creating an empty model,
	 * which we always do.
	 */
	private void initializeEmbeddedTypeFromDefault(IStructuredModel model) {
		EmbeddedTypeHandler embeddedContentType = getJSPDefaultEmbeddedType();
		initializeEmbeddedType(model, embeddedContentType);
	}

	/**
	 * This is "initialize" since is always assumes it hasn't been initalized
	 * yet.
	 */
	private void initializeEmbeddedType(IStructuredModel model, EmbeddedTypeHandler embeddedContentType) {
		// check program logic
		Assert.isNotNull(embeddedContentType, "Program error: invalid call during model initialization"); //$NON-NLS-1$
		// once we know the embedded content type, we need to set it in the
		// PageDirectiveAdapter ... the order of initialization is
		// critical here, the doc must have been created, but its contents not
		// set yet,
		// and all factories must have been set up also.
		XMLModel domModel = (XMLModel) model;
		XMLDocument document = domModel.getDocument();
		PageDirectiveAdapter pageDirectiveAdapter = (PageDirectiveAdapter) document.getAdapterFor(PageDirectiveAdapter.class);
		pageDirectiveAdapter.setEmbeddedType(embeddedContentType);
		embeddedContentType.initializeFactoryRegistry(model.getFactoryRegistry());
		IStructuredDocument structuredDocument = model.getStructuredDocument();
		embeddedContentType.initializeParser((JSPCapableParser) structuredDocument.getParser());
		// adding language here, in this convienent central
		// location, but some obvious renaming or refactoring
		// wouldn't hurt, in future.
		// I needed to add this language setting for JSP Fragment support
		// Note: I don't think this attempted init counts for much.
		// I think its always executed when model is very first
		// being initialized, and doesn't even have content
		// or an ID yet. I thought I'd leave, since it wouldn't
		// hurt, in case its called in other circumstances.
		//		String language = getLanguage(model);
		//		pageDirectiveAdapter.setLanguage(language);
	}

	/**
	 * Method getLanguage.
	 * 
	 * @param model
	 * @return String
	 */
	private String getLanguage(IStructuredModel model) {
		String result = null;
		// first check the model (document itself) to see if contains
		result = getLanguageFromStructuredDocument(model.getStructuredDocument());
		// Note: if model contains an unsupported
		// language, we'll even return it,
		// since who knows what future holds.

		// always return something
		if (result == null) {
			result = DEFAULT_LANGUAGE;
		}
		return result;
	}

	/**
	 * Method getLanguageFromStructuredDocument.
	 * 
	 * @param structuredDocument
	 * @return String
	 */
	private String getLanguageFromStructuredDocument(IStructuredDocument structuredDocument) {
		if (structuredDocument == null)
			return null;
		String result = null;
		// bascially same algorithm as get encoding or
		// get content type from structuredDocument.
		IJSPHeadContentDetector localHeadParser = getHeadParser();
		// we can be assured that its already been
		// parsed. If not call parseHeaderForPageDirective()
		// before calling getLanguage;
		localHeadParser.set(structuredDocument);
		try {
			result = localHeadParser.getLanguage();
		}
		catch (IOException e) {
			//impossible
			// TODO need to reconsider design to avoid
			throw new Error(e);
		}
		return result;
	}

	/**
	 * This is "reinitialize" since there should always be at least the
	 * default one assigned, before we start checking the stream
	 */
	private void reInitializeEmbeddedType(IStructuredModel model, EmbeddedTypeHandler oldEmbeddedContentType, EmbeddedTypeHandler newEmbeddedContentType) {
		// check program logic
		Assert.isNotNull(oldEmbeddedContentType, "Program error: invalid call during model initialization"); //$NON-NLS-1$
		// once we know the embedded content type, we need to set it in the
		// PageDirectiveAdapter ... the order of initialization is
		// critical here, the doc must have been created, but its contents not
		// set yet,
		// and all factories must have been set up also.
		XMLModel domModel = (XMLModel) model;
		IStructuredDocument structuredDocument = model.getStructuredDocument();
		XMLDocument document = domModel.getDocument();
		PageDirectiveAdapter pageDirectiveAdapter = (PageDirectiveAdapter) document.getExistingAdapter(PageDirectiveAdapter.class);
		// ==> // PropagatingAdapter propagatingAdapter = (PropagatingAdapter)
		// ((INodeNotifier)
		// document).getExistingAdapter(PropagatingAdapter.class);
		// ==> // ModelQueryAdapter modelQueryAdapter = (ModelQueryAdapter)
		// ((INodeNotifier)
		// document).getExistingAdapter(ModelQueryAdapter.class);
		oldEmbeddedContentType.uninitializeFactoryRegistry(model.getFactoryRegistry());
		oldEmbeddedContentType.uninitializeParser((JSPCapableParser) structuredDocument.getParser());
		// since 'document' is not recreated in this
		// reinit path, we need to remove all adapters,
		// except for the propagated adapters (including page
		// directive adapter, and model query adapter).
		// to accomplish this, we'll just remove all, then
		// add back with a call to pre-load adapt.
		// let clients decide to unload adapters from document
		//		Collection oldAdapters = document.getAdapters();
		//		Iterator oldAdaptersIterator = oldAdapters.iterator();
		//		while (oldAdaptersIterator.hasNext()) {
		//			INodeAdapter oldAdapter = (INodeAdapter)
		// oldAdaptersIterator.next();
		//			if (oldAdapter != pageDirectiveAdapter && oldAdapter !=
		// propagatingAdapter && oldAdapter != modelQueryAdapter) {
		//				// DO NOT remove directly!
		//				// can change contents while in notifity loop!
		//				//oldAdaptersIterator.remove();
		//				document.removeAdapter(oldAdapter);
		//			}
		//		}
		// DMW: I believe something like the following is needed,
		// since releases cached adapters
		//				if (document instanceof DocumentImpl) {
		//					((DocumentImpl) document).releaseDocumentType();
		//					((DocumentImpl) document).releaseStyleSheets();
		//				}
		// remember, embedded type factories are automatically cleared when
		// embededType changed
		pageDirectiveAdapter.setEmbeddedType(newEmbeddedContentType);
		//		// but still need to clear the page directive watchers, and let
		// them be rediscovered (with new, accurate node as target)
		//		pageDirectiveAdapter.clearPageWatchers();
		if (newEmbeddedContentType != null) {
			newEmbeddedContentType.initializeFactoryRegistry(model.getFactoryRegistry());
			newEmbeddedContentType.initializeParser((JSPCapableParser) structuredDocument.getParser());

			// partitioner setup is the responsibility of this loader
			if (newEmbeddedContentType.getFamilyId().equals(IContentTypeIdentifier.ContentTypeID_SSEXML)) {
				((StructuredTextPartitionerForJSP) structuredDocument.getDocumentPartitioner()).setEmbeddedPartitioner(new StructuredTextPartitionerForXML());
			}
			else if (newEmbeddedContentType.getFamilyId().equals(IContentTypeIdentifier.ContentTypeID_HTML)) {
				((StructuredTextPartitionerForJSP) structuredDocument.getDocumentPartitioner()).setEmbeddedPartitioner(new StructuredTextPartitionerForHTML());
			}
		}
		// adding language here, in this convienent central
		// location, but some obvious renaming or refactoring
		// wouldn't hurt, in future.
		// I needed to add this language setting for JSP Fragment support
		// Note: this is the one that counts, since at this point,
		// the model has an ID, so we can look up IFile, etc.
		String language = getLanguage(model);
		if (language != null && language.length() > 0) {
			pageDirectiveAdapter.setLanguage(language);
		}
	}

	/**
	 * This is "reinitialize" since there should always be at least the
	 * default one assigned, before we start checking the stream
	 */
	private void initCloneOfEmbeddedType(IStructuredModel model, EmbeddedTypeHandler oldEmbeddedContentType, EmbeddedTypeHandler newEmbeddedContentType) {
		// check program logic
		Assert.isNotNull(oldEmbeddedContentType, "Program error: invalid call during model initialization"); //$NON-NLS-1$
		// once we know the embedded content type, we need to set it in the
		// PageDirectiveAdapter ... the order of initialization is
		// critical here, the doc must have been created, but its contents not
		// set yet,
		// and all factories must have been set up also.
		XMLModel domModel = (XMLModel) model;
		IStructuredDocument structuredDocument = model.getStructuredDocument();
		XMLDocument document = domModel.getDocument();
		PageDirectiveAdapter pageDirectiveAdapter = (PageDirectiveAdapter) document.getAdapterFor(PageDirectiveAdapter.class);
		// ==> // PropagatingAdapter propagatingAdapter = (PropagatingAdapter)
		// ((INodeNotifier) document).getAdapterFor(PropagatingAdapter.class);
		// ==> // ModelQueryAdapter modelQueryAdapter = (ModelQueryAdapter)
		// ((INodeNotifier) document).getAdapterFor(ModelQueryAdapter.class);
		// because, even in the clone case, the model has been paritally
		// intialized with
		// the old embedded type (during createModel), we need to unitialize
		// parts of it, based on the old (or default) ones
		oldEmbeddedContentType.uninitializeFactoryRegistry(model.getFactoryRegistry());
		oldEmbeddedContentType.uninitializeParser((JSPCapableParser) structuredDocument.getParser());
		// remember, embedded type factories are automatically cleared when
		// embededType changed
		pageDirectiveAdapter.setEmbeddedType(newEmbeddedContentType);
		if (newEmbeddedContentType != null) {
			newEmbeddedContentType.initializeFactoryRegistry(model.getFactoryRegistry());
			newEmbeddedContentType.initializeParser((JSPCapableParser) structuredDocument.getParser());
		}
		// adding language here, in this convienent central
		// location, but some obvious renaming or refactoring
		// wouldn't hurt, in future.
		// I needed to add this language setting for JSP Fragment support
		// Note: this is the one that counts, since at this point,
		// the model has an ID, so we can look up IFile, etc.
		String language = getLanguage(model);
		if (language != null && language.length() > 0) {
			pageDirectiveAdapter.setLanguage(language);
		}
	}

	private EmbeddedTypeHandler getEmbeddedType(IStructuredModel model) {
		Document doc = ((XMLModel) model).getDocument();
		PageDirectiveAdapter pageDirectiveAdapter = (PageDirectiveAdapter) ((INodeNotifier) doc).getAdapterFor(PageDirectiveAdapter.class);
		EmbeddedTypeHandler embeddedHandler = pageDirectiveAdapter.getEmbeddedType();
		return embeddedHandler;
	}

	/**
	 * Method initEmbeddedType.
	 */
	protected void initEmbeddedType(IStructuredModel model) {
		initializeEmbeddedTypeFromDefault(model);
	}

	/**
	 * Method initEmbeddedType.
	 */
	protected void initEmbeddedType(IStructuredModel oldModel, IStructuredModel newModel) {
		EmbeddedTypeHandler existingEmbeddedType = getEmbeddedType(oldModel);
		EmbeddedTypeHandler newEmbeddedContentType = existingEmbeddedType.newInstance();
		if (existingEmbeddedType == null) {
			initEmbeddedType(newModel);
		}
		else {
			//initEmbeddedType(newModel);
			initCloneOfEmbeddedType(newModel, existingEmbeddedType, newEmbeddedContentType);
		}
		setLanguageInPageDirective(newModel);
	}

	protected void setLanguageInPageDirective(IStructuredModel newModel) {
		if (newModel instanceof XMLModel) {
			XMLDocument document = ((XMLModel) newModel).getDocument();
			PageDirectiveAdapter pageDirectiveAdapter = (PageDirectiveAdapter) document.getAdapterFor(PageDirectiveAdapter.class);
			String language = getLanguage(newModel);
			pageDirectiveAdapter.setLanguage(language);
		}
	}

	public IStructuredModel reinitialize(IStructuredModel model) {
		EmbeddedTypeHandler oldHandler = null;
		EmbeddedTypeHandler newHandler = null;
		Object reinitStateData = model.getReinitializeStateData();
		if (reinitStateData instanceof EmbeddedTypeStateData) {
			EmbeddedTypeStateData oldStateData = (EmbeddedTypeStateData) reinitStateData;
			oldHandler = oldStateData.getOldHandler();
			newHandler = oldStateData.getNewHandler();
			// note. We should already have the new handler in the model's
			// (documents) adapters,
			// so need need to use the old one to undo the old state data
			reInitializeEmbeddedType(model, oldHandler, newHandler);
		}
		else {
			// for language ... we someday MIGHT have to do something
			// here, but for now, we don't have any model-side language
			// sensitive adapters.
		}
		return super.reinitialize(model);
	}

	public ModelLoader newInstance() {
		return new JSPModelLoader();
	}

	public IDocumentLoader getDocumentLoader() {
		if (documentLoaderInstance == null) {
			documentLoaderInstance = new JSPDocumentLoader();
		}
		return documentLoaderInstance;
	}

	/**
	 * Ensures that an InputStream has mark/reset support.
	 */
	public static InputStream getMarkSupportedStream(InputStream original) {
		if (original == null)
			return null;
		if (original.markSupported())
			return original;
		return new BufferedInputStream(original);
	}

	protected byte[] getBytes(InputStream inputStream, int max) throws IOException {
		byte[] smallBuffer = new byte[max];
		byte[] returnBuffer = null;
		int nRead = inputStream.read(smallBuffer, 0, max);
		if (nRead < max) {
			// empty file will return -1;
			if (nRead < 0)
				nRead = 0;
			byte[] smallerBuffer = new byte[nRead];
			System.arraycopy(smallBuffer, 0, smallerBuffer, 0, nRead);
			returnBuffer = smallerBuffer;
		}
		else {
			returnBuffer = smallBuffer;
		}
		return returnBuffer;
	}


}