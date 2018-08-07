//
// 이 파일은 JAXB(JavaTM Architecture for XML Binding) 참조 구현 2.2.8-b130911.1802 버전을 통해 생성되었습니다. 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>를 참조하십시오. 
// 이 파일을 수정하면 소스 스키마를 재컴파일할 때 수정 사항이 손실됩니다. 
// 생성 날짜: 2018.08.07 시간 12:13:45 PM KST 
//


package com.tmaxsoft.schemas.anylink;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bizSystemDefaultThreadPoolId",
    "systemLogging",
    "nodeList"
})
@XmlRootElement(name = "bizSystemInfo")
public class BizSystemInfo {

    @XmlElement(required = true)
    protected String bizSystemDefaultThreadPoolId;
    @XmlElement(required = true)
    protected BizSystemInfo.SystemLogging systemLogging;
    @XmlElement(required = true)
    protected BizSystemInfo.NodeList nodeList;

    /**
     * bizSystemDefaultThreadPoolId 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBizSystemDefaultThreadPoolId() {
        return bizSystemDefaultThreadPoolId;
    }

    /**
     * bizSystemDefaultThreadPoolId 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBizSystemDefaultThreadPoolId(String value) {
        this.bizSystemDefaultThreadPoolId = value;
    }

    /**
     * systemLogging 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link BizSystemInfo.SystemLogging }
     *     
     */
    public BizSystemInfo.SystemLogging getSystemLogging() {
        return systemLogging;
    }

    /**
     * systemLogging 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link BizSystemInfo.SystemLogging }
     *     
     */
    public void setSystemLogging(BizSystemInfo.SystemLogging value) {
        this.systemLogging = value;
    }

    /**
     * nodeList 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link BizSystemInfo.NodeList }
     *     
     */
    public BizSystemInfo.NodeList getNodeList() {
        return nodeList;
    }

    /**
     * nodeList 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link BizSystemInfo.NodeList }
     *     
     */
    public void setNodeList(BizSystemInfo.NodeList value) {
        this.nodeList = value;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "node"
    })
    public static class NodeList {

        @XmlElement(required = true)
        protected BizSystemInfo.NodeList.Node node;

        /**
         * node 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link BizSystemInfo.NodeList.Node }
         *     
         */
        public BizSystemInfo.NodeList.Node getNode() {
            return node;
        }

        /**
         * node 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link BizSystemInfo.NodeList.Node }
         *     
         */
        public void setNode(BizSystemInfo.NodeList.Node value) {
            this.node = value;
        }


        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "name"
        })
        public static class Node {

            @XmlElement(required = true)
            protected String name;

            /**
             * name 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * name 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

        }

    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "logLevel"
    })
    public static class SystemLogging {

        @XmlElement(required = true)
        protected String logLevel;

        /**
         * logLevel 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLogLevel() {
            return logLevel;
        }

        /**
         * logLevel 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLogLevel(String value) {
            this.logLevel = value;
        }

    }

}
