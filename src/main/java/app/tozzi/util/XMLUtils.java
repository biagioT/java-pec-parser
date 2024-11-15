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

public class XMLUtils {

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
