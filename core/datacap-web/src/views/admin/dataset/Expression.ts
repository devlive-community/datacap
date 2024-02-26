export enum Expression
{
  SUM = 'SUM',
  COUNT = 'COUNT',
  MAX = 'MAX',
  MIN = 'MIN',
  AVG = 'AVG',
  // Filter Expression
  EQ = 'EQ',
  NE = 'NEQ',
  IS_NULL = 'NULL',
  IS_NOT_NULL = 'NNULL',
  IS_LIKE = 'LIKE',
  IS_NOT_LIKE = 'NLIKE',
  IS_IN = 'IS_IN',
  IS_NOT_IN = 'IS_NOT_IN',
  IS_CONTAINS = 'IS_CONTAINS',
  IS_NOT_CONTAINS = 'IS_NOT_CONTAINS',
}
