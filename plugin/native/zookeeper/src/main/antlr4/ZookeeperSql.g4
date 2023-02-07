grammar ZookeeperSql;

singleStatement : statement EOF ;

statement : 'SELECT' selectElements fromClause ;

fromClause : 'FROM' qualifiedName;

selectElements : ID|'*' ;

ID : [a-zA-Z]+ ;

WS : [ \r\n\t]+ -> skip ;

QUOTED_IDENTIFIER
    : '"' ( ~'"' | '""' )* '"'
    ;

BACKQUOTED_IDENTIFIER
    : '`' ( ~'`' | '``' )* '`'
    ;

qualifiedName
    : identifier ('.' identifier)*
    ;

identifier
    : IDENTIFIER
    | QUOTED_IDENTIFIER      #quotedIdentifier
    | BACKQUOTED_IDENTIFIER  #backQuotedIdentifier
    | DIGIT_IDENTIFIER       #digitIdentifier
    ;
