## @ModelAttribute

### paramter level

> * https://donggu1105.tistory.com/14

1. 해당 parameter의 객체를 생성
   * 해당 class는 IOC container에 의해 관리되는 Bean이어야 하고, getter와 setter가 생성돼 있어야 한다.
2. parameter binding을 알아서 해준다.
3. view로 해당 데이터를 전달

### method level

> * https://www.baeldung.com/spring-mvc-and-the-modelattribute-annotation
> * https://memo-the-day.tistory.com/200

```
@ModelAttribute("regions")
public Map<String, String> regions() {
    Map<String, String> regions = new LinkedHashMap<>();
    regions.put("SEOUL", "서울");
    regions.put("BUSAN", "부산");
    regions.put("JEJU", "제주");
    return regions;
}
```

* 만약 해당 controller의 모든 @RequestMapping에 공통 데이터가 필요하다면 매번 model.addAttribute(data)를 해줘야한다.
  * @method level에 @ModelAttribute를 달아주면 모든 @RequestMapping에 공통 data를 지정해 줄 수 있다.
* 물론 성능상으로는 static으로 지정해놓고 필요할 때만 불러오는 것이 이득이지만, 이정도는 성능 최적화에 크게 영향을 끼치지 않는다.
* annotation이 달린 @RequestMapping으로 mapping된 메서드가 호출되기 전에 해당 @ModelAttribute가 달린 메서드가 먼저 호출된다.
* 즉, 데이터를 먼저 생성해야 할 때 사용할 수 있다.

