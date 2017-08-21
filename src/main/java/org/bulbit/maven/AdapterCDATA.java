package org.bulbit.maven;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Java XML serialisation extension allowing to process CDATA sections.
 *
 * @author Janis Upitis
 */
public class AdapterCDATA extends XmlAdapter<String, String> {

    @Override
    public String marshal(String arg0) throws Exception {
        return "<![CDATA[" + arg0 + "]]>";
    }
    @Override
    public String unmarshal(String arg0) throws Exception {
        return arg0;
    }
}
