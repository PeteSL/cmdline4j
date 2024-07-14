# cmdline4j
Command line parser using Java StreamTokenizer

This is a method that parses most command lines using java.io.SreamTokenizer respecting quoted strings.

It does not keep multiple consecutive spaces and is restricted to UTF-8 (or ASCII) as StreamTokenizer only works on 0x0000-0x00ff.
