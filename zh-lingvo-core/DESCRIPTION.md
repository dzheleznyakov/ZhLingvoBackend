```
SPEC ::= ENTRY*
ENTRY ::= NAME VALUE
VALUE ::= NAME | "NAME*" | [VALUE*] | {ENTRY*}
NAME ::= string
```