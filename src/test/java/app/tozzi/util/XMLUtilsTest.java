package app.tozzi.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

import static org.junit.jupiter.api.Assertions.*;

public class XMLUtilsTest {

    private Document document;

    @BeforeEach
    void setUp() throws Exception {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        document = builder.newDocument();
        var root = document.createElement("root");
        document.appendChild(root);
        var child1 = document.createElement("child");
        child1.setAttribute("id", "1");
        child1.setTextContent("First Child");
        root.appendChild(child1);
        var child2 = document.createElement("child");
        child2.setAttribute("id", "2");
        child2.setTextContent("Second Child");
        root.appendChild(child2);
        var child3 = document.createElement("child");
        child3.setTextContent("Third Child");
        root.appendChild(child3);
    }

    @Test
    void testGetAttribute() throws XPathExpressionException {
        var attribute = XMLUtils.getAttribute(document, "/root/child", "id");
        assertTrue(attribute.isPresent());
        assertEquals("1", attribute.get());
    }

    @Test
    void testGetAttributeNotFound() throws XPathExpressionException {
        var attribute = XMLUtils.getAttribute(document, "/root/child", "nonexistent");
        assertTrue(attribute.isEmpty());
    }

    @Test
    void testGetTextAndAttribute() throws XPathExpressionException {
        var result = XMLUtils.getTextAndAttribute(document, "/root/child", "id");
        assertEquals(3, result.size());
        assertEquals("1", result.get("First Child"));
        assertEquals("2", result.get("Second Child"));
        assertNull(result.get("Third Child"));
    }

    @Test
    void testGetTextContent() throws XPathExpressionException {
        var textContent = XMLUtils.getTextContent(document, "/root/child");
        assertTrue(textContent.isPresent());
        assertEquals("First Child", textContent.get());
    }

    @Test
    void testGetTextContentNotFound() throws XPathExpressionException {
        var textContent = XMLUtils.getTextContent(document, "/root/nonexistent");
        assertTrue(textContent.isEmpty());
    }

    @Test
    void testGetNodesEmpty() throws XPathExpressionException {
        var nodes = XMLUtils.getTextContent(document, "/root/nonexistent");
        assertTrue(nodes.isEmpty());
    }
}
