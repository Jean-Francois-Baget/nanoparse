# 🧬 nanoparse

**nanoparse** is a lightweight, composable parsing library written in Java.  
It lets you define grammars directly in code and parse complex structures with minimal boilerplate.

> ⚠️ This project is a work in progress. Its API and internal structure may evolve in future versions.

---

## ✨ Features

- Define grammars with Java constructs (no external grammar files required)
- Modular readers: regex, string, sequence, choice, repetition, optional, etc.
- Fully navigable parse trees (`IMatch`)
- Lightweight & fast — no unnecessary dependencies
- Supports recursive and left-recursive grammars
- Ready for integration with reasoning engines and domain-specific languages (e.g. DLGPE)

---

## 🚀 Installation

Since this project is not yet available on Maven Central, you can build and install it locally:

```bash
git clone https://gitlab.inria.fr/jfbaget/nanoparse.git
cd nanoparse
mvn clean install
```

Then add it as a dependency in your pom.xml:

```xml
<dependency>
  <groupId>fr.inria.jfbaget.jfbaget</groupId>
  <artifactId>nanoparse</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```
---
## 🧪 Toy Example: Basic Rule Grammar

Here's how to define and use a basic rule parser:

```java
import java.util.List;
import java.util.Map;

import fr.inria.jfbaget.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.jfbaget.nanoparse.readers.*;

public class BRParser extends Parser {
		
	private static List<IReader> readers() {
		return List.of(
			new RepetitionReader("main", "item", null, 0, Integer.MAX_VALUE, false),
			new ChoiceReader("item", List.of("fact", "rule"), false),
			new SequenceReader("fact", List.of("ID", "dot"), false, 0),
			new StringReader("dot", ".", true),
			new SequenceReader("rule", List.of("ID", "rulesep", "list", "dot"), false, Map.of(0, "head", 2, "body")),
			new StringReader("rulesep", ":-", true),
			new RepetitionReader("list", "ID", "comma", 0, Integer.MAX_VALUE, false),
			new StringReader("comma", ",", true));
	}	
	
	public BRParser() {
		super(readers());
	}
	
	public static void main(String[] args) {
		
		BRParser parser =  new BRParser();
		String input = "a :- b, c, d, e.";
		IMatch match = parser.read(input, 0);
		System.out.println(match.success());
	}
}
```

🤝 Contributions
This library is being developed as part of ongoing research at Inria and may later evolve into a more widely distributed module.

Pull requests and feedback are welcome — especially if you have grammars or DSLs you’d like to build with it.

📜 License
🄯 2025 Jean-François Baget, Inria
Licensed under the CECILL license (if you choose to add one later).