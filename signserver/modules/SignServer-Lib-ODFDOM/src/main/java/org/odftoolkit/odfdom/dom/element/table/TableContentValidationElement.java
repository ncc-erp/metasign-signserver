/************************************************************************
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 *
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0. You can also
 * obtain a copy of the License at http://odftoolkit.org/docs/license.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ************************************************************************/

/*
 * This file is automatically generated.
 * Don't edit manually.
 */    

package org.odftoolkit.odfdom.dom.element.table;

import org.odftoolkit.odfdom.OdfName;
import org.odftoolkit.odfdom.OdfNamespace;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.dom.OdfNamespaceNames;
import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.dom.attribute.table.TableNameAttribute;
import org.odftoolkit.odfdom.dom.attribute.table.TableConditionAttribute;
import org.odftoolkit.odfdom.dom.attribute.table.TableBaseCellAddressAttribute;
import org.odftoolkit.odfdom.dom.attribute.table.TableAllowEmptyCellAttribute;
import org.odftoolkit.odfdom.dom.attribute.table.TableDisplayListAttribute;

import org.odftoolkit.odfdom.dom.element.office.OfficeEventListenersElement;

/**
 * DOM implementation of OpenDocument element  {@odf.element table:content-validation}.
 *
 */
public abstract class TableContentValidationElement extends OdfElement
{        
    public static final OdfName ELEMENT_NAME = OdfName.get( OdfNamespace.get(OdfNamespaceNames.TABLE), "content-validation" );


	/**
	 * Create the instance of <code>TableContentValidationElement</code> 
	 *
	 * @param  ownerDoc     The type is <code>OdfFileDom</code>
	 */
	public TableContentValidationElement( OdfFileDom ownerDoc )
	{
		super( ownerDoc, ELEMENT_NAME	);
	}

	/**
	 * Get the element name 
	 *
	 * @return  return   <code>OdfName</code> the name of element {@odf.element table:content-validation}.
	 */
	public OdfName getOdfName()
	{
		return ELEMENT_NAME;
	}

	/**
	 * Initialization of the mandatory attributes of {@link  TableContentValidationElement}
	 *
     * @param tableNameAttributeValue  The mandatory attribute {@odf.attribute  table:name}"
     *
	 */
	public void init(String tableNameAttributeValue)
	{
	}

	/**
	 * Receives the value of the ODFDOM attribute representation <code>TableNameAttribute</code> , See {@odf.attribute table:name}
	 *
	 * @return - the <code>String</code> , the value or <code>null</code>, if the attribute is not set and no default value defined.
	 */
	public String getTableNameAttribute()
	{
		TableNameAttribute attr = (TableNameAttribute) getOdfAttribute( OdfName.get( OdfNamespace.get(OdfNamespaceNames.TABLE), "name" ) );
		if( attr != null ){
			return String.valueOf( attr.getValue() );
		}
		return null;
	}
		 
	/**
	 * Sets the value of ODFDOM attribute representation <code>TableNameAttribute</code> , See {@odf.attribute table:name}
	 *
	 * @param tableNameValue   The type is <code>String</code>
	 */
	public void setTableNameAttribute( String tableNameValue )
	{
		TableNameAttribute attr =  new TableNameAttribute( (OdfFileDom)this.ownerDocument );
		setOdfAttribute( attr );
		attr.setValue( tableNameValue );
	}


	/**
	 * Receives the value of the ODFDOM attribute representation <code>TableConditionAttribute</code> , See {@odf.attribute table:condition}
	 *
	 * @return - the <code>String</code> , the value or <code>null</code>, if the attribute is not set and no default value defined.
	 */
	public String getTableConditionAttribute()
	{
		TableConditionAttribute attr = (TableConditionAttribute) getOdfAttribute( OdfName.get( OdfNamespace.get(OdfNamespaceNames.TABLE), "condition" ) );
		if( attr != null ){
			return String.valueOf( attr.getValue() );
		}
		return null;
	}
		 
	/**
	 * Sets the value of ODFDOM attribute representation <code>TableConditionAttribute</code> , See {@odf.attribute table:condition}
	 *
	 * @param tableConditionValue   The type is <code>String</code>
	 */
	public void setTableConditionAttribute( String tableConditionValue )
	{
		TableConditionAttribute attr =  new TableConditionAttribute( (OdfFileDom)this.ownerDocument );
		setOdfAttribute( attr );
		attr.setValue( tableConditionValue );
	}


	/**
	 * Receives the value of the ODFDOM attribute representation <code>TableBaseCellAddressAttribute</code> , See {@odf.attribute table:base-cell-address}
	 *
	 * @return - the <code>String</code> , the value or <code>null</code>, if the attribute is not set and no default value defined.
	 */
	public String getTableBaseCellAddressAttribute()
	{
		TableBaseCellAddressAttribute attr = (TableBaseCellAddressAttribute) getOdfAttribute( OdfName.get( OdfNamespace.get(OdfNamespaceNames.TABLE), "base-cell-address" ) );
		if( attr != null ){
			return String.valueOf( attr.getValue() );
		}
		return null;
	}
		 
	/**
	 * Sets the value of ODFDOM attribute representation <code>TableBaseCellAddressAttribute</code> , See {@odf.attribute table:base-cell-address}
	 *
	 * @param tableBaseCellAddressValue   The type is <code>String</code>
	 */
	public void setTableBaseCellAddressAttribute( String tableBaseCellAddressValue )
	{
		TableBaseCellAddressAttribute attr =  new TableBaseCellAddressAttribute( (OdfFileDom)this.ownerDocument );
		setOdfAttribute( attr );
		attr.setValue( tableBaseCellAddressValue );
	}


	/**
	 * Receives the value of the ODFDOM attribute representation <code>TableAllowEmptyCellAttribute</code> , See {@odf.attribute table:allow-empty-cell}
	 *
	 * @return - the <code>Boolean</code> , the value or <code>null</code>, if the attribute is not set and no default value defined.
	 */
	public Boolean getTableAllowEmptyCellAttribute()
	{
		TableAllowEmptyCellAttribute attr = (TableAllowEmptyCellAttribute) getOdfAttribute( OdfName.get( OdfNamespace.get(OdfNamespaceNames.TABLE), "allow-empty-cell" ) );
		if( attr != null ){
			return Boolean.valueOf( attr.booleanValue() );
		}
		return Boolean.valueOf( TableAllowEmptyCellAttribute.DEFAULT_VALUE );
	}
		 
	/**
	 * Sets the value of ODFDOM attribute representation <code>TableAllowEmptyCellAttribute</code> , See {@odf.attribute table:allow-empty-cell}
	 *
	 * @param tableAllowEmptyCellValue   The type is <code>Boolean</code>
	 */
	public void setTableAllowEmptyCellAttribute( Boolean tableAllowEmptyCellValue )
	{
		TableAllowEmptyCellAttribute attr =  new TableAllowEmptyCellAttribute( (OdfFileDom)this.ownerDocument );
		setOdfAttribute( attr );
		attr.setBooleanValue( tableAllowEmptyCellValue.booleanValue() );
	}


	/**
	 * Receives the value of the ODFDOM attribute representation <code>TableDisplayListAttribute</code> , See {@odf.attribute table:display-list}
	 *
	 * @return - the <code>String</code> , the value or <code>null</code>, if the attribute is not set and no default value defined.
	 */
	public String getTableDisplayListAttribute()
	{
		TableDisplayListAttribute attr = (TableDisplayListAttribute) getOdfAttribute( OdfName.get( OdfNamespace.get(OdfNamespaceNames.TABLE), "display-list" ) );
		if( attr != null ){
			return String.valueOf( attr.getValue() );
		}
		return TableDisplayListAttribute.DEFAULT_VALUE;
	}
		 
	/**
	 * Sets the value of ODFDOM attribute representation <code>TableDisplayListAttribute</code> , See {@odf.attribute table:display-list}
	 *
	 * @param tableDisplayListValue   The type is <code>String</code>
	 */
	public void setTableDisplayListAttribute( String tableDisplayListValue )
	{
		TableDisplayListAttribute attr =  new TableDisplayListAttribute( (OdfFileDom)this.ownerDocument );
		setOdfAttribute( attr );
		attr.setValue( tableDisplayListValue );
	}

	/**
	 * Create child element {@odf.element table:help-message}.
	 *
	 * @return   return  the element {@odf.element table:help-message}
	 * DifferentQName 
	 */
	public TableHelpMessageElement newTableHelpMessageElement()
	{
		TableHelpMessageElement  tableHelpMessage = ((OdfFileDom)this.ownerDocument).newOdfElement(TableHelpMessageElement.class);
		this.appendChild( tableHelpMessage);
		return  tableHelpMessage;
	}                   
               
	/**
	 * Create child element {@odf.element table:error-message}.
	 *
	 * @return   return  the element {@odf.element table:error-message}
	 * DifferentQName 
	 */
	public TableErrorMessageElement newTableErrorMessageElement()
	{
		TableErrorMessageElement  tableErrorMessage = ((OdfFileDom)this.ownerDocument).newOdfElement(TableErrorMessageElement.class);
		this.appendChild( tableErrorMessage);
		return  tableErrorMessage;
	}                   
               
	/**
	 * Create child element {@odf.element table:error-macro}.
	 *
	 * @return   return  the element {@odf.element table:error-macro}
	 * DifferentQName 
	 */
	public TableErrorMacroElement newTableErrorMacroElement()
	{
		TableErrorMacroElement  tableErrorMacro = ((OdfFileDom)this.ownerDocument).newOdfElement(TableErrorMacroElement.class);
		this.appendChild( tableErrorMacro);
		return  tableErrorMacro;
	}                   
               
	/**
	 * Create child element {@odf.element office:event-listeners}.
	 *
	 * @return   return  the element {@odf.element office:event-listeners}
	 * DifferentQName 
	 */
	public OfficeEventListenersElement newOfficeEventListenersElement()
	{
		OfficeEventListenersElement  officeEventListeners = ((OdfFileDom)this.ownerDocument).newOdfElement(OfficeEventListenersElement.class);
		this.appendChild( officeEventListeners);
		return  officeEventListeners;
	}                   
               
}
