

## TODO

- DONE: pk,fk要出string版本的 xxxInput給網頁用
- DONE: 抓schema default value後放到@AttrubiuteInfo
- DONE: 抓schema default value後放到source code的field `private String myValue = "1";`
- DONE: TableComment in class
- DONE: 可以手動加入relation，讓產生出來的table可以有relation
- DONE: Table annotation  and comment is up side down
- DONE: 所有型別在 input mode 應該都要變成 String  (只有date需要)
- DONE: extend from base class
- DONE: only one relation map for all table
- DONE: ~~Option Validation -> allow Optional<T> ?~~ rejected
- DONE: ManyToOne 的@JoinColumn name會少了UUID
- DONE: ~~generate relation map from DB~~ rejected, no need, do this manually
- DONE: add unique key to Column.java
- DONE: ~~name conflict , ex: item1, item2~~
- DONE: ~~Input object should not extends from any class~~ reject , depends by config
- DONE: default value = BigDecimal.ZERO = 0.0 , BigDeciaml.value(1.1) = 1.1
- DONE: update input
- DONE: add @NotNull to reference field (rejected : @NotNull跟@ManyToOne混用 會引起validate 時奇怪的問題)
- DONE: 日期改成用 Date, @DateTimeFormat(pattern = "yyyy-MM-dd")
- DONE: Master-Detail ? (rejected 儘量減少對master-detail的依賴)
- DONE: input to dto package (可自行選擇，export config?)
- TODO: fetch ignore columns from base class 
- TODO: json config
- TODO: Query Object
- TODO: Repository
- TODO: Service
- TODO: DomainFactory
- TODO: Controller
- TODO: ControllerTest
- TODO: Master-Detail Controller
- TODO: Master-Detail ControllerTest 
- TODO: Master-Detail-Extend Controller
- TODO: Master-Detail-Extend ControllerTest

