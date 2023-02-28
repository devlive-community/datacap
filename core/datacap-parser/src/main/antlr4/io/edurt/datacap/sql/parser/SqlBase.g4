grammar SqlBase;

singleStatement:(statement)*;

SHOW: [Ss][Hh][Oo][Ww];
PATHS: [Pp][Aa][Tt][Hh][Ss];
SELECT: [Ss][Ee][Ll][Ee][Cc][Tt];
FROM: [Ff][Rr][Oo][Mm];

statement
    : SHOW childPathStatement
    | SELECT columnStatement fromClause
    ;

// SHOW PATHS, SHOW PATHS FROM ...
childPathStatement: PATHS | PATHS fromClause;

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
