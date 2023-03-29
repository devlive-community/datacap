grammar SqlBase;

singleStatement:(statement)*;

SHOW: [Ss][Hh][Oo][Ww];
PATHS: [Pp][Aa][Tt][Hh][Ss];
TOPICS: 'TOPICS';
CONSUMERS: 'CONSUMERS';
DATABASES: [Dd][Aa][Tt][Aa][Bb][Aa][Ss][Es][Ss];
TABLES: [Tt][Aa][Bb][Ll][Ee][Ss];
SELECT: [Ss][Ee][Ll][Ee][Cc][Tt];
FROM: [Ff][Rr][Oo][Mm];

statement
    : SHOW childPathStatement
    | SELECT columnStatement fromClause
    | kafkaStatement
    ;

// -------------------- Kafka Statement --------------------
kafkaQueryTopicStatement
    : SHOW TOPICS
    | SHOW DATABASES
    ;
kafkaQueryConsumerStatement
    : SHOW CONSUMERS
    | SHOW CONSUMERS fromClause
    | SHOW TABLES
    | SHOW TABLES fromClause
    ;
kafkaQueryStatement
    : kafkaQueryTopicStatement
    | kafkaQueryConsumerStatement
    ;
kafkaStatement
    : kafkaQueryStatement
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
