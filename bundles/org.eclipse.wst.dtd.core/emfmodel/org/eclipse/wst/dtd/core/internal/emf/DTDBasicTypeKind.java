/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.dtd.core.internal.emf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Basic Type Kind</b></em>',
 * and utility methods for working with them. <!-- end-user-doc -->
 * 
 * @see org.eclipse.wst.dtd.core.internal.emf.DTDPackage#getDTDBasicTypeKind()
 * @model
 * @generated
 */
public final class DTDBasicTypeKind extends AbstractEnumerator {
	/**
	 * The '<em><b>NONE</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #NONE_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NONE = 0;

	/**
	 * The '<em><b>CDATA</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #CDATA_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CDATA = 1;

	/**
	 * The '<em><b>ID</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #ID_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ID = 2;

	/**
	 * The '<em><b>IDREF</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #IDREF_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int IDREF = 3;

	/**
	 * The '<em><b>IDREFS</b></em>' literal value. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #IDREFS_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int IDREFS = 4;

	/**
	 * The '<em><b>ENTITY</b></em>' literal value. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #ENTITY_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ENTITY = 5;

	/**
	 * The '<em><b>ENTITIES</b></em>' literal value. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #ENTITIES_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ENTITIES = 6;

	/**
	 * The '<em><b>NMTOKEN</b></em>' literal value. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #NMTOKEN_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NMTOKEN = 7;

	/**
	 * The '<em><b>NMTOKENS</b></em>' literal value. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #NMTOKENS_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NMTOKENS = 8;

	/**
	 * The '<em><b>NONE</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NONE</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #NONE
	 * @generated
	 * @ordered
	 */
	public static final DTDBasicTypeKind NONE_LITERAL = new DTDBasicTypeKind(NONE, "NONE");

	/**
	 * The '<em><b>CDATA</b></em>' literal object. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>CDATA</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #CDATA
	 * @generated
	 * @ordered
	 */
	public static final DTDBasicTypeKind CDATA_LITERAL = new DTDBasicTypeKind(CDATA, "CDATA");

	/**
	 * The '<em><b>ID</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ID</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ID
	 * @generated
	 * @ordered
	 */
	public static final DTDBasicTypeKind ID_LITERAL = new DTDBasicTypeKind(ID, "ID");

	/**
	 * The '<em><b>IDREF</b></em>' literal object. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>IDREF</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #IDREF
	 * @generated
	 * @ordered
	 */
	public static final DTDBasicTypeKind IDREF_LITERAL = new DTDBasicTypeKind(IDREF, "IDREF");

	/**
	 * The '<em><b>IDREFS</b></em>' literal object. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>IDREFS</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #IDREFS
	 * @generated
	 * @ordered
	 */
	public static final DTDBasicTypeKind IDREFS_LITERAL = new DTDBasicTypeKind(IDREFS, "IDREFS");

	/**
	 * The '<em><b>ENTITY</b></em>' literal object. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>ENTITY</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ENTITY
	 * @generated
	 * @ordered
	 */
	public static final DTDBasicTypeKind ENTITY_LITERAL = new DTDBasicTypeKind(ENTITY, "ENTITY");

	/**
	 * The '<em><b>ENTITIES</b></em>' literal object. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>ENTITIES</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ENTITIES
	 * @generated
	 * @ordered
	 */
	public static final DTDBasicTypeKind ENTITIES_LITERAL = new DTDBasicTypeKind(ENTITIES, "ENTITIES");

	/**
	 * The '<em><b>NMTOKEN</b></em>' literal object. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>NMTOKEN</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #NMTOKEN
	 * @generated
	 * @ordered
	 */
	public static final DTDBasicTypeKind NMTOKEN_LITERAL = new DTDBasicTypeKind(NMTOKEN, "NMTOKEN");

	/**
	 * The '<em><b>NMTOKENS</b></em>' literal object. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>NMTOKENS</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #NMTOKENS
	 * @generated
	 * @ordered
	 */
	public static final DTDBasicTypeKind NMTOKENS_LITERAL = new DTDBasicTypeKind(NMTOKENS, "NMTOKENS");

	/**
	 * An array of all the '<em><b>Basic Type Kind</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final DTDBasicTypeKind[] VALUES_ARRAY = new DTDBasicTypeKind[]{NONE_LITERAL, CDATA_LITERAL, ID_LITERAL, IDREF_LITERAL, IDREFS_LITERAL, ENTITY_LITERAL, ENTITIES_LITERAL, NMTOKEN_LITERAL, NMTOKENS_LITERAL,};

	/**
	 * A public read-only list of all the '<em><b>Basic Type Kind</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Basic Type Kind</b></em>' literal with the
	 * specified name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static DTDBasicTypeKind get(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			DTDBasicTypeKind result = VALUES_ARRAY[i];
			if (result.toString().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Basic Type Kind</b></em>' literal with the
	 * specified value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static DTDBasicTypeKind get(int value) {
		switch (value) {
			case NONE :
				return NONE_LITERAL;
			case CDATA :
				return CDATA_LITERAL;
			case ID :
				return ID_LITERAL;
			case IDREF :
				return IDREF_LITERAL;
			case IDREFS :
				return IDREFS_LITERAL;
			case ENTITY :
				return ENTITY_LITERAL;
			case ENTITIES :
				return ENTITIES_LITERAL;
			case NMTOKEN :
				return NMTOKEN_LITERAL;
			case NMTOKENS :
				return NMTOKENS_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private DTDBasicTypeKind(int value, String name) {
		super(value, name);
	}

} // DTDBasicTypeKind
