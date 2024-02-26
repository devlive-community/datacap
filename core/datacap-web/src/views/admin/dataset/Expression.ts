export enum Expression
{
  SUM = 'SUM',
  COUNT = 'COUNT',
  MAX = 'MAX',
  MIN = 'MIN',
  AVG = 'AVG',
  // Filter Expression
  EQ = 'EQ',
  NE = 'NE',
  IS_NULL = 'NULL',
  IS_NOT_NULL = 'NNULL',
  IS_IN = 'IS_IN',
  IS_NOT_IN = 'IS_NOT_IN',
  IS_LIKE = 'IS_LIKE',
  IS_NOT_LIKE = 'IS_NOT_LIKE',
  IS_CONTAINS = 'IS_CONTAINS',
  IS_NOT_CONTAINS = 'IS_NOT_CONTAINS',
}
