grammar SqlBase;

singleStatement:(statement)*;

SELECT: [Ss][Ee][Ll][Ee][Cc][Tt];
FROM: [Ff][Rr][Oo][Mm];

statement
    : SELECT columnStatement fromClause
    ;

columnStatement: identifier;

fromClause : FROM tableName;

tableName: identifier ('.' identifier)*;
identifier: (IDENTIFIER | STRING | quotedIdentifier)*;
quotedIdentifier: BACKQUOTED_IDENTIFIER;

fragment DIGIT:[0-9];
fragment LETTER:[a-zA-Z];
STRING
    : '\'' ( ~('\''|'\\') | ('\\' .) )* '\''
    | '"' ( ~('"'|'\\') | ('\\' .) )* '"'
    | '*'
    ;
IDENTIFIER
    : (LETTER | DIGIT | '_')+
    ;
BACKQUOTED_IDENTIFIER
    : '`' ( ~'`' | '``' )* '`'
    ;

SIMPLE_COMMENT: '--' ~[\r\n]* '\r'? '\n'? -> channel(HIDDEN);
BRACKETED_EMPTY_COMMENT: '/**/' -> channel(HIDDEN);
BRACKETED_COMMENT : '/*' ~[+] .*? '*/' -> channel(HIDDEN);
WS: [ \r\n\t]+ -> channel(HIDDEN);
