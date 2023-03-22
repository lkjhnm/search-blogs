# search-blogs (SBS Application)

### Executable Module
https://github.com/lkjhnm/search-blogs/raw/main/dist/sbs-web-1.0.0-SNAPSHOT.jar

### 외부 라이브러리
- Retrofit2 : REST API 연계를 위한 REST Client 모듈로서 사용
- hibernate-validator : 효율적인 요청 파라미터 검증을 위해 사용
- lombok : POJO 클래스의 getter/setter/toString/builder 자동화를 위해 사용

### API 명세

#### 블로그 검색

<pre>
    <code>
    GET /v1/search/blog
    </code>
</pre>

##### Request
|Name|Type|Description|Required|
|------|---|---|---|
|query|String|검색을 원하는 질의어|O|
|page|Integer|검색 결과의 페이지 번호, 1~50 사이의 값, 기본값 1|X|
|size|Integer|표시할 검색 결과 개수, 1~50 사이의 값, 기본값 10|X|
|sort|String|검색 결과 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy|X

##### Response

###### meta
|Name|Type|Description|
|------|---|---|
|total|Integer|질의어로 검색된 결과 중 표시 가능한 총 결과 개수|
|end|Boolean|현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음|

###### blogs
|Name|Type|Description|
|------|---|---|
|title|String|블로그 글 제목|
|contents|String|블로그 글 요약|
|url|String|블로그 글 URL|
|name|String|블로그의 이름|
|postdate|Datetime|블로그 글 작성시간,  ISO-8601 [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss]|
|extras|Object|블로그에 대한 추가 정보|

##### Example
<pre>
    <code>
    {
      "meta": {
        "total": 1,
        "end": true
      },
      "blogs": [
        {
          "title": "작은 <b>집</b> <b>짓기</b> 기본컨셉 - <b>집</b><b>짓기</b> 초기구상하기",
          "contents": "이 점은 <b>집</b>을 지으면서 고민해보아야 한다. 하지만, 금액에 대한 가성비 대비 크게 문제되지 않을 부분이라 생각하여 설계로 극복하자고 생각했다. 전체 <b>집</b><b>짓기</b>의 기본방향은 크게 세 가지이다. 우선은 여가의 영역 증대이다. 현대 시대 일도 중요하지만, 여가시간 <b>집</b>에서 어떻게 보내느냐가 중요하니깐 이를 기본적...",
          "url": "https://brunch.co.kr/@tourism/91",
          "name": "정란수의 브런치",
          "postdate": "2017-05-07T18:50:07",
          "extras": {
            "thumbnail": "http://search3.kakaocdn.net/argon/130x130_85_c/7r6ygzbvBDc"
          }
        },
        ...
      ]
    }
    </code>
</pre>

---

#### 인기 키워드 통계 조회
<pre>
    <code>
    GET /v1/statistics/keyword/popular
    </code>
</pre>

##### Request
|Name|Type|Description|Required|
|------|---|---|---|
|size|Integer|인기 키워드 표시 개수, 1~10 사이의 값, 기본 값 10|X|

##### Response
|Name|Type|Description|
|------|---|---|
|keyword|String|키워드|
|count|Integer|키워드 검색 횟수|

##### Example
<pre>
    <code>
    [
      {
        "keyword": "test1",
        "count": 10
      },
      ...
    ]
    </code>
</pre>