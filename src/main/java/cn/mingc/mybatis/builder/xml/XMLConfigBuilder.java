package cn.mingc.mybatis.builder.xml;

import cn.mingc.mybatis.datasource.DataSourceFactory;
import cn.mingc.mybatis.io.Resources;
import cn.mingc.mybatis.mapping.Environment;
import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.mapping.MappedStatement.Builder;
import cn.mingc.mybatis.mapping.SqlCommandType;
import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.transaction.TransactionFactory;
import cn.mingc.mybatis.type.TypeAliasRegistry;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLConfigBuilder {

    private Configuration configuration;

    private Element root;

    public XMLConfigBuilder(Reader reader) {
        configuration = new Configuration();
        try {
            this.root = new SAXReader().read(reader).getRootElement();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration parse() {
        try {
            this.environmentElement(this.root.element("environments"));
            this.mapperElement(this.root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this.configuration;
    }

    private void environmentElement(Element environments) throws Exception {
        String defaultEnv = environments.attributeValue("default");
        List<Element> environmentElementList = environments.elements("environment");
        TypeAliasRegistry typeAliasRegistry = this.configuration.getTypeAliasRegistry();
        for (Element environmentElement : environmentElementList) {
            String id = environmentElement.attributeValue("id");
            if (!defaultEnv.equals(id)) {
                continue;
            }
            Element transactionManagerElement = environmentElement.element("transactionManager");
            TransactionFactory transactionFactory = (TransactionFactory) typeAliasRegistry.resolveAlias(
                    transactionManagerElement.attributeValue("type")).newInstance();
            Element datasourceElement = environmentElement.element("datasource");
            DataSourceFactory datasourceFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(
                    datasourceElement.attributeValue("type")).newInstance();
            Properties properties = new Properties();
            List<Element> propertyElementList = datasourceElement.elements("property");
            for (Element propertyElement : propertyElementList) {
                properties.put(propertyElement.attributeValue("name"), propertyElement.attributeValue("value"));
            }
            datasourceFactory.setProperties(properties);

            Environment environment = new Environment.Builder(id).transactionFactory(transactionFactory)
                    .dataSource(
                            datasourceFactory.getDataSource()).build();
            this.configuration.setEnvironment(environment);
        }
    }

    private void mapperElement(Element mappers) throws Exception {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element element : mapperList) {
            String mapperResource = element.getText();

            // 读取mapper.xml
            Element mapperRoot = null;
            Reader mapperReader = Resources.getResourceAsReader(mapperResource);
            mapperRoot = new SAXReader().read(mapperReader).getRootElement();
            // 名称空间
            String namespace = mapperRoot.attributeValue("namespace");
            Class<?> mapperInterface = Class.forName(namespace);

            // select 语句
            List<Element> selectElementList = mapperRoot.elements("select");
            for (Element node : selectElementList) {
                String sql = node.getText();
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");

                Map<Integer, String> parameter = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 1; matcher.find(); i++) {
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    parameter.put(i, g2);
                    sql = sql.replace(g1, "?");
                }

                String msid = namespace + "." + id;
                MappedStatement statement = new Builder(msid, SqlCommandType.SELECT, sql).resultType(
                        resultType).parameterType(parameterType).parameter(parameter).build();

                configuration.addMappedStatement(statement);
            }

            this.configuration.addMapper(mapperInterface);
        }
    }
}
