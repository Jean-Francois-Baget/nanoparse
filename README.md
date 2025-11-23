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
- Supports recursive grammars
- Ready for integration with reasoning engines and domain-specific languages

---

## 🚀 Installation


### Installation from gitlab repository

```bash
git clone https://gitlab.inria.fr/jfbaget/nanoparse.git
cd nanoparse
mvn clean install
```
### Getting it from Maven Central

Add it as a dependency in your pom.xml:

```xml
<dependency>
  <groupId>io.github.jean-francois-baget</groupId>
  <artifactId>nanoparse</artifactId>
  <version>0.1.0</version>
</dependency>
``` 

## Javadoc

Can be found on https://javadoc.io/doc/io.github.jean-francois-baget/nanoparse/latest/index.html



---
## 🧪 Toy Example: Basic Rule Grammar

Here's how to define and use a basic rule parser:

```java
import java.util.List;
import java.util.Map;

import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.readers.*;

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
		System.out.println(match.toJSON());
	}
}
```
In the above code, the `IMatch` structure can be traversed to gather the data needed. The printed
JSON display is there for debugging and informational purpose only.

## Requirements
* Java 17+ (compiled with `--release 17`).
* `org.json` is used for optional JSON output of matches (`IMatch.toJSON()`).


## 🤝 Contributions

This library is being developed as part of ongoing research at Inria and may later evolve into a more widely distributed module.

Pull requests and feedback are welcome — especially if you have grammars or DSLs you’d like to build with it.

## 📜 License

🄯 2025 Jean-François Baget, Inria
Licensed under the Apache 2 license.