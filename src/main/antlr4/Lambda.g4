grammar Lambda;

//start variable
input: (term|VARIABLE)+ EOF
    ;

term: LPAREN (SINGLEBOUND)+ (VARIABLE|term)+ RPAREN         #SingleBound
    | LPAREN  MULTIBOUND (VARIABLE|term)+ RPAREN            #MultiBound
    | LPAREN (term)+ RPAREN                                 #TermOnly
    | LPAREN (term)+ (VARIABLE)+ RPAREN                     #TermVariable   //(term|variable)+ ?
    | LPAREN VARIABLE (VARIABLE)+ RPAREN                    #Variables
    ;

// Tokens
VARIABLE: [a-z];
SINGLEBOUND: LAMBDA [a-z] DOT;
MULTIBOUND: LAMBDA [a-z]+ DOT;
LAMBDA: 'L'
    | 'λ'
    ;
LPAREN: '(';
RPAREN: ')';
DOT: '.';
WS: [ \t\n]+ -> skip;

