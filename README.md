
# STUST南台學生助理 ( STUST Student Assistant )
南台學生助手 ( STUST Student Assistant )  整合「南臺消息」、「南臺數位學習系統 ( FLIP / FLIP CLASS )」、「南臺選課系統」中的部分功能與資訊，幫助學生能夠快速瀏覽相關訊，包括「課表」、「課程」、「作業」等內容，目前部分功能尚在測試階段，有相關問題歡迎回報。
請使用校內共通帳戶進行登入。
![STUST-Student-Assistant](https://user-images.githubusercontent.com/62971778/110233062-731e0c80-7f5c-11eb-8cc3-783f0c1196c9.png)
## 版本資訊
當前版本：v1.0.1
## 功能介紹
### 課表瀏覽
* **自動追蹤日期**<br>
<br>根據當天日期，課表起始焦點將會自動移動至對應標號。
<br>如下圖所示：測試日為 `3/7(日)` 因此畫面將會停在`標號 7 `<br>
<br>![CurriculumDemo1](https://user-images.githubusercontent.com/62971778/110233610-01e05880-7f60-11eb-8207-6bdd70b6b40f.gif)

* **完整課表內容**<br>
<br>您可以透過`點擊`或`滑動`來變更焦點項目，並且如果`點擊`項目與焦點項目相同則會開啟完整內容。
<br>如下圖所示：<br>
<br>![CurriculumDemo2](https://user-images.githubusercontent.com/62971778/110234350-f9d6e780-7f64-11eb-857d-0490a8e1db36.gif)

* ~**_顯示上課位置_**~**_( 尚未推出 )_**<br>
<br>~_您可以透過再次`點擊`完整內容中的目標項目來顯示課程所在位置_~。

### 課程瀏覽
* **支援 [Flip](https://flip.stust.edu.tw/) 以及 [Flip Class](https://flipclass.stust.edu.tw/) 課程內容瀏覽**<br>
<br>能夠透過`左右滑動`切換 [Flip](https://flip.stust.edu.tw/) 與 [Flip Class](https://flipclass.stust.edu.tw/)，`點擊`後能夠顯示該課程詳細內容。
<br>如下圖所示：<br>
<br>![CourseDemo1](https://user-images.githubusercontent.com/62971778/110236448-e0d43380-7f70-11eb-9820-c50b90a177be.gif)

* **課程內容瀏覽與附件下載**<br>
<br>在目前支援的項目內容中，能夠使用`長按`來顯示目標的完整名稱，並且可以透過`點擊`來瀏覽目標詳細內容。
<br>如下圖所示：<br>
<br>![CourseDemo2](https://user-images.githubusercontent.com/62971778/110241632-c0b26d80-7f8c-11eb-975c-9a6c09bbc5e2.gif)

* **支援部分內容瀏覽與附件下載**<br> 
<br>在「課程活動」、「課程教材」、「課程作業」中目前支援瀏覽的項目如下：<br>
**_注意：目前並沒有支援「上傳/編輯」等相關功能_**
  * ![ppt](https://user-images.githubusercontent.com/62971778/110240037-ecc9f080-7f84-11eb-94d7-8d671bf9b5bc.png) **[ Flip ]** 簡報教材 ( ppt ) 
  * ![hyperlink](https://user-images.githubusercontent.com/62971778/110240041-f2273b00-7f84-11eb-8655-b1f11caaad30.png) **[ Flip ]** 外部連結 ( hyperlink )
  * ![homework(successful)](https://user-images.githubusercontent.com/62971778/110240044-f5bac200-7f84-11eb-97a6-a81950c1de4d.png) **[ Flip ]** 已繳交課程作業 ( exercise successful )
  * ![mp3](https://user-images.githubusercontent.com/62971778/110240052-ffdcc080-7f84-11eb-98a3-3fa4c51bda22.png) **[ Flip / Flip Class ]** 音訊教材 ( mp3 )
  * ![doc](https://user-images.githubusercontent.com/62971778/110240055-0834fb80-7f85-11eb-8eee-bfa84231af0c.png) **[ Flip / Flip Class ]** 一般文件教材 ( doc )
  * ![homework](https://user-images.githubusercontent.com/62971778/110240057-0d924600-7f85-11eb-8194-bb282e8f6ad0.png) **[ Flip / Flip Class ]** 課程作業 ( exercsie ) 
  * ![pdf](https://user-images.githubusercontent.com/62971778/110238436-95278700-7f7c-11eb-888e-c0e9ecbcb1d6.png) **[ Flip / Flip Class ]** 可攜式文件教材 ( pdf )
  * ![ifram](https://user-images.githubusercontent.com/62971778/110240080-269af700-7f85-11eb-9d50-b58a4653ee0b.png) **[ Flip / Flip Class ]** 嵌入式網頁教材 ( iframe )
  * ![Media 1](https://user-images.githubusercontent.com/62971778/110240087-2c90d800-7f85-11eb-9e8a-36121e375030.png) **[ Flip / Flip Class ]** 外部影片連結教材 ( embed )
  * ![default](https://user-images.githubusercontent.com/62971778/110240117-5518d200-7f85-11eb-8bbe-d79f1e3e86b1.png) ~**[ Flip Class]** EverCam~ ( 尚未支援 )

### 公告瀏覽
* **支援 [Flip](https://flip.stust.edu.tw/) 以及 [Flip Class](https://flipclass.stust.edu.tw/) 課程公告以及「南台消息」瀏覽**<br>
<br>介面中會顯示最新的前5項公告資訊，`點擊`目標則會開啟公告瀏覽視窗，如公告項目總數超出5項，則會在右上方顯示「顯示全部」功能按鍵，`點擊`後則會跳轉至完整的公告頁面。
<br>如下圖所示：<br>
<br>![NewsDemo1](https://user-images.githubusercontent.com/62971778/110242081-420aff80-7f8f-11eb-9d61-5975a1736db6.gif)

### 通知瀏覽
* **支援 [Flip](https://flip.stust.edu.tw/) 以及 [Flip Class](https://flipclass.stust.edu.tw/) 課程作業與測驗通知**<br>
<br>介面中能夠透過左右`滑動`來切換 [Flip](https://flip.stust.edu.tw/) 以及 [Flip Class](https://flipclass.stust.edu.tw/) ，並且`點擊`項目之後則會顯示通知內容。
<br>如下圖所示：<br>
<br>![MessageDemo1](https://user-images.githubusercontent.com/62971778/111063415-47b19980-84e9-11eb-9776-4edaa2be8a6b.gif)


* ~**_通知提醒功能_**~**_( 尚未推出 )_**<br>
<br>~_您可以透過個人設定項目中的「通知提醒」自訂通知提醒時間，系統將會在到期日前提發送通知醒您_~。

### 個人設定
* **帳號資訊以及相關設定**<br>
<br>能夠查看當前使用者的用戶資訊，並且能根據使用者習慣更改其設定內容。<br><br>
  * **用戶資訊** — 查看當前使用者資訊。
  * **通知提醒** — 設定通知提醒時間。
  * **初始畫面** — 設定初始畫面項目。
  * **同步課表** — 系統啟動時會將已取得課表與課程內容進行比對，如不相符則會再次取得課表內容。
  * **離線模式** — 啟用此模式將不會檢測網路狀態，會直接採用本地課表進行瀏覽。
<br><br>![PersonalDemo1](https://user-images.githubusercontent.com/62971778/111063409-42ece580-84e9-11eb-8add-c456bb8b241d.gif)


