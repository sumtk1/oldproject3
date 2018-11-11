package com.zyd.wlwsdk.utlis.xmlanalsis;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hygo03 on 2017/3/27.
 */
public class XmlParserHelper extends DefaultHandler {

    /**
     * 存储所有的解析对象
     */
    private List<ProvinceModel> provinceList = new ArrayList<>();

    public XmlParserHelper() {

    }

    public List<ProvinceModel> getDataList() {
        return provinceList;
    }

    @Override
    public void startDocument() throws SAXException {
        // 当读到第一个开始标签的时候，会触发这个方法
    }

    ProvinceModel provinceModel = new ProvinceModel();
    CityModel cityModel = new CityModel();
    String tagName=null;

    private StringBuffer bf = new StringBuffer();

    /** 遇到元素节点开始时候的处理方法 */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tagName = localName;
        // 当遇到开始标记的时候，调用这个方法
        if("province".equals(tagName)){
            provinceModel = new ProvinceModel();
            provinceModel.setCityList(new ArrayList<CityModel>());
        }else if("cities".equals(tagName)){
            cityModel = new CityModel();
        }
        bf.setLength(0);
    }

    /** 遇到文本节点时的操作 */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        bf.append(ch, start, length);//存入文本节点的值
    }

    /** 遇到元素节点结束时候的操作 */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // 遇到结束标记的时候，会调用这个方法
        if ("city".equals(qName)) {
            cityModel.setName(bf.toString());
            provinceModel.getCityList().add(cityModel);
//            L.e("---endElement2--", cityModel.toString() + "--1");
        } else if ("state".equals(qName)) {
            provinceModel.setName(bf.toString());
//            L.e("---endElement3--", provinceModel.toString() + "--1");
            provinceList.add(provinceModel);
        }
    }


}
