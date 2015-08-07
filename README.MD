

## TODO
- json config
- Master-Detail-Extend Controller
- Master-Detail-Extend ControllerTest

## DONE
- enum comment support both `=` and `:`
- fix expend issue : product -> proproduct, address -> addraddress
- integer, boolean 的default value要判斷型別，而不是都用字串
- 日期格式裡的中文註解沒format時，沒有產生對應的@DateTimeFormat -> 要求一定要有format
- ~~remove reference table, using table mapping instead~~ rejected
- Master-Detail Controller
- Master-Detail ControllerTest 
- configable topPackage
- ~~fetch ignore columns from base class~~ rejected, no need 
- gen constants form option
- Repository
- Query Object
- Service
- DomainFactory
- Controller
- ControllerTest
- pk,fk要出string版本的 xxxInput給網頁用
- 抓schema default value後放到@AttrubiuteInfo
- 抓schema default value後放到source code的field `private String myValue = "1";`
- TableComment in class
- 可以手動加入relation，讓產生出來的table可以有relation
- Table annotation  and comment is up side down
- 所有型別在 input mode 應該都要變成 String  (只有date需要)
- extend from base class
- only one relation map for all table
- ~~Option Validation -> allow Optional<T> ?~~ rejected
- ManyToOne 的@JoinColumn name會少了UUID
- ~~generate relation map from DB~~ rejected, no need, do this manually
- add unique key to Column.java
- ~~name conflict , ex: item1, item2~~
- ~~Input object should not extends from any class~~ reject , depends by config
- default value = BigDecimal.ZERO = 0.0 , BigDeciaml.value(1.1) = 1.1
- update input
- add @NotNull to reference field (rejected : @NotNull跟@ManyToOne混用 會引起validate 時奇怪的問題)
- 日期改成用 Date, @DateTimeFormat(pattern = "yyyy-MM-dd")
- Master-Detail ? (rejected 儘量減少對master-detail的依賴)
- input to dto package (可自行選擇，export config?)
