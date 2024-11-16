package app.tozzi.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author Biagio Tozzi
 */
public class XMLUtils {

    /**
     * Extracts attribute value from {@link Document}
     *
     * @param doc document
     * @param path path
     * @param attributeName attribute
     * @return attribute value
     * @throws XPathExpressionException
     */
    public static Optional<String> getAttribute(Document doc, String path, String attributeName) throws XPathExpressionException {

        return getNodes(doc, path).stream()
                .findFirst()
                .flatMap(node -> {
                    var attributes = node.getAttributes();
                    for (var i = 0; i < attributes.getLength(); i++) {
                        if (attributes.item(i).getNodeName().equalsIgnoreCase(attributeName)) {
                            return Optional.ofNullable(attributes.item(i).getNodeValue());
                        }
                    }
                    return Optional.empty();
                })
                .or(Optional::empty);
    }

    /**
     * Extracts text content and node value of attribute from {@link Document}
     *
     * @param document document
     * @param path path
     * @param attribute attribute
     * @return text and attribute
     * @throws XPathExpressionException
     */
    public static Map<String, String> getTextAndAttribute(Document document, String path, String attribute) throws XPathExpressionException {
        var result = new HashMap<String, String>();
        var nodes = getNodes(document, path);

        nodes.forEach(node -> {
            var value = node.getTextContent();
            String attr = null;

            var attributes = node.getAttributes();
            for (int j = 0; j < attributes.getLength(); j++) {
                if (attributes.item(j).getNodeName().equalsIgnoreCase(attribute)) {
                    attr = attributes.item(j).getNodeValue();
                    break;
                }
            }
            result.put(value, attr);
        });

        return result;
    }

    /**
     * Extracts text content from {@link Document}
     *
     * @param document document
     * @param path path
     * @return text attribute
     * @throws XPathExpressionException
     */
    public static Optional<String> getTextContent(Document document, String path) throws XPathExpressionException {
        return getNodes(document, path).stream()
                .findFirst()
                .map(Node::getTextContent)
                .or(Optional::empty);
    }

    private static List<Node> getNodes(Document document, String path) throws XPathExpressionException {
        var expr = XPathFactory.newInstance().newXPath().compile(path);
        var nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        return IntStream.range(0, nodes.getLength())
                .mapToObj(nodes::item)
                .toList();
    }

}
