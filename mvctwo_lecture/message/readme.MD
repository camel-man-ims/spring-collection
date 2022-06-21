# 메세지, 국제화

## 메세지

* resources > `messages.properties` 를 선언하면 해당 파일에 있는 변수들을 전역변수로 사용할 수 있다.
  * `#{변수명}` 으로 사용하면 된다.  

## 국제화

* `Accept-language` 라는 header의 `attribute` 를 통해 해당 값을 추출한다.
  * en이면 영어 우선, ko면 한국어 우선이다.

<hr/>

## Error

* ### 한글 깨짐 현상

* 왜 일어나는지 모르겠다.
  * [링크](https://www.inflearn.com/chats/242157)를 참조해서 해봤는데도 일단 안된다.