# redis-distributed-lock

## Condition
1. SL과 같은 infinite try 개념 사용 X
2. 대신 pub/sub구조로 single thread 기반 redis에 부하 감소

## Case 1. 동일 서버 내에서 Thread간 리소스 접근 제어 샘플
- SetnxLockRunner: Thread간 stock 개수 제어
- MultiTransactionRunner: 멀티 트랜잭션 제어
  - 트랜잭션은 multi()에 대한 atomic 연산을 보장할 수 없으므로 독립적인 자원 control을 보장하지 못함.


## Case 2. 다른 서버 끼리, Process간 리소스 접근 제어

