# redis-distributed-lock
<<<<<<< HEAD
레디스 분산 락 샘플

=======

## Condition
1. SL과 같은 infinite try 개념 사용 X
2. 대신 pub/sub구조로 single thread 기반 redis에 부하 감소

## Case 1. 동일 서버 내에서 Thread간 리소스 접근 제어 샘플
- SetnxLockRunner: Thread간 stock 개수 제어
- MultiTransactionRunner: 멀티 트랜잭션 제어


## Case 2. 다른 서버 끼리, Process간 리소스 접근 제어

