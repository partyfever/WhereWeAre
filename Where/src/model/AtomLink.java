package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Link which represents the HATEOAS structure
 * @author Sebastian
 *
 */
@XmlRootElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
public class AtomLink {
	public @XmlAttribute
	String rel;
	public @XmlAttribute
	String href;
	public @XmlAttribute
	String type;

	public AtomLink() {
	}

	public AtomLink(String rel, String href, String type) {
		this.rel = rel;
		this.href = href;
		this.type = type;
	}

	public AtomLink(String rel, String href) {
		this(rel, href, "application/json");
	}
}

