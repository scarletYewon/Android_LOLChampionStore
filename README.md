# [2022-2 모바일 프로그래밍]

## 💻 소프트웨어학부 20213047 이예원

# 🎮 LOL Champion Store 🎮

리그오브레전드(롤) 챔피언 상점

간단한 로그인과 챔피언 리스트를 볼 수 있는 안드로이드 앱입니다.

# Environment

- macOs
- Kotlin
- Android Studio
- Firebase
- AVD: Pixel 2 API 31

# SDK Version

- SDK version : **Android 12(API 수준 31, 32)**
- compileSdk : 32
- minSdk : 31
- targetSdk : 32

# Description

Data Class File : Users.kt

```kotlin
data class Users(
    var newId: String = "newId",
    var newPw: String = "newPw",
    var name: String = "name",
    var number: String = "number",
    var address: String = "address",
    var accept: Boolean
)
```

Firebase Authentication
<img width="1065" alt="Auth" src="https://user-images.githubusercontent.com/81704418/198977158-af7c4f94-99a3-4383-aa3a-ac3a2dee573f.png">

Firebase realtime database
<img width="1160" alt="db" src="https://user-images.githubusercontent.com/81704418/198977163-4e35fe99-ed0d-4870-848e-c8ffacc7c1f3.png">

## 화면 구성 : MainActivity, SecondActivity, ThirdActivity, MyinfoActivity

### MainActivity : 로그인

- Relative Layout
- 앱을 실행하면 뜨는 첫번째 화면
- ‘로그인’버튼, ‘회원가입’버튼, ‘비회원으로 구경하기’ 버튼으로 구성
- 로그인에 성공하면 상품 리스트 화면(ThirdActivity)로 이동
- 회원가입 버튼 클릭시 회원가입 화면(SecondActivity)으로 이동
- 비회원으로 구경하기 버튼 클릭 시 비회원 상태로 상품 리스트 화면(ThirdActivity)으로 이동

### SecondActivity : 회원가입

- Linear Layout
- 회원정보를 입력하면 Firebase realtime database로 정보를 전달하고 계정 생성
- [이메일, 비밀번호, 이름, 전화번호, 주소, 개인정보 수집 및 이용 동의 여부] 총 6개의 항목으로 구성
- 모든 칸을 채워야 회원가입 가능
- 이메일은 이메일 형식을 따라야 하고 비밀번호는 숫자,영어 포함 6자리 이상
- ‘약관보기’ 버튼 클릭시 상세 약관 보기 가능
- 개인정보 수집 동의 시에만 회원가입 가능

### ThirdActivity : 상품 리스트

- Grid Layout
- 6개의 상품이 보여짐
- 회원정보버튼을 클릭하면 MyinfoActivity로 이동
- font : 배달의민족 주아체

### MyinfoActivity : 내 정보

- Linear Layout
- 회원가입시 입력한 이메일, 이름, 전화번호, 주소 정보를 보여줌
- Firebase에서 제공하는 realtime database에서 데이터를 받아옴
- 비회원일 경우 회원가입 페이지(SecondActivity)로 이동
- font : 배달의민족 주아체

# 실행화면

- 로그인(MainActivity)
<img width="354" alt="login" src="https://user-images.githubusercontent.com/81704418/198977175-c520f680-24c2-4025-aec8-c7e187f5e047.png">

- 회원가입(SecondActivity)
<img width="865" alt="register" src="https://user-images.githubusercontent.com/81704418/198977184-57052993-0cce-43b1-9841-44d2861460c9.png">

- 상품 리스트
<img width="348" alt="main" src="https://user-images.githubusercontent.com/81704418/198977177-988dd4aa-69d5-45e9-a748-30d661f83173.png">

- 회원정보
<img width="348" alt="info" src="https://user-images.githubusercontent.com/81704418/198977170-cda18bbd-cb47-4413-b6dd-2e82b40b4bc6.png">

# 주요 코드 및 기능 설명

- 로그인

```kotlin
// 로그인
    fun signinEmail() {
        auth?.signInWithEmailAndPassword(binding.email.text.toString(),binding.pw.text.toString())
            ?.addOnCompleteListener {
                    task ->
                if(task.isSuccessful) {
                    // Login, 아이디와 패스워드가 맞았을 때
                    moveMainPage(task.result?.user)
                } else {
                    // Show the error message, 아이디와 패스워드가 틀렸을 때
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
        val am: AccountManager = AccountManager.get(this) // "this" references the current Context
        val accounts: Array<out Account> = am.getAccountsByType("com.google")
    }
    // 로그인이 성공하면 다음 페이지로 넘어가는 함수
    private fun moveMainPage(user: FirebaseUser?) {
        // 파이어베이스 유저 상태가 있을 경우 다음 페이지로 넘어갈 수 있음
        if(user != null) {
            val intent = (Intent(this, ThirdActivity::class.java))
            startActivity(intent)
        }
    }
```

- 회원가입(비밀번호 규칙)

```kotlin
private fun createAccount(newId: String, newPw: String, name: String, phone: String, address: String, accept: Boolean) {
        var eng = false
        var num = false
        for (i: Int in 0 until newPw.length) {
            if (isDigit(newPw.get(i))) {
                num = true
            } else if (0x61.toChar() <= newPw.get(i) && newPw.get(i) <= 0x7A.toChar() || 0x41.toChar() <= newPw.get(
                    i
                ) && newPw.get(i) <= 0x5A.toChar()
            ) {
                eng = true
            }
            if (eng && num) {
                break
            }
        }
        if (newId.isNotEmpty() && newPw.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() && accept == true && num == true && eng == true) {
            auth?.createUserWithEmailAndPassword(newId, newPw)
                ?.addOnCompleteListener(this) { task ->
                    val uid = FirebaseAuth.getInstance().uid ?:""
                    if (task.isSuccessful) {
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference()
                        val dataInput = Users(
                            binding.newId.text.toString(),
                            binding.newPw.text.toString(),
                            binding.name.text.toString(),
                            binding.number.text.toString(),
                            binding.address.text.toString(),
                            binding.accept.isChecked
                        )
                        myRef.child(uid).setValue(dataInput)
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()

                        moveMainPage(task.result?.user)
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        else if (eng == false or num == false){
            Toast.makeText(
                this, "영어와 숫자를 포함하여 6자리 이상 입렵해주세요",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if (accept == false){
            Toast.makeText(
                this, "개인정보 수집 및 이용에 동의해주세요",
                Toast.LENGTH_SHORT
            ).show()
        }
        else {
            Toast.makeText(
                this, "모든 칸을 채워주새요",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
```

- Popup

```kotlin
val show_Btn = findViewById<Button>(R.id.show_Btn)
        show_Btn.setOnClickListener {
            showPopup()
        }
private fun showPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("개인정보 수집 및 이용 안내")
        builder.setMessage("<개인정보보호 포털> 개인정보 처리방침\n" +
                "회원가입을 위해서는 아래와 같이 개인정보를 수집 및 이용합니다.\n" +
                "\n" +
                "1. 개인정보 수집 목적: 회원관리\n" +
                "\n" +
                "2. 개인정보 수집 항목 : 이메일, 비밀번호, 이름, 전화번호, 주소\n" +
                "\n" +
                "3. 보유 및 이용기간: 회원 탈퇴 시까지\n" +
                "\n" +
                "위 개인정보 수집 및 이용을 확인합니다.")
        builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i:Int->}
        builder.show()
    }
```

- 회원여부 확인

```kotlin
//myinfo(회원정보)버튼 클릭시 memberorNot함수 실행
binding.myInfo.setOnClickListener {
            memberOrNot(a)
        }
private fun memberOrNot(a: Boolean) {
        if(a == false) {
            val intent = (Intent(this, MyinfoActivity::class.java))
            startActivity(intent)
        }
        else{
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }
```

- Firebase에서 데이터 받아와 출력

```kotlin
class MyinfoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myinfo)

        val mail = findViewById<TextView>(R.id.emailText)
        val name = findViewById<TextView>(R.id.nameText)
        val number = findViewById<TextView>(R.id.numberText)
        val address = findViewById<TextView>(R.id.addressText)
        auth = Firebase.auth
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        userDB = Firebase.database.reference
        val currentUserDB = userDB.child(getCurrentUserId())

        currentUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
						// 데이터를 받아와서 TextView에 setText해줌
                mail.text = snapshot.child("newId").value.toString()
                name.text = snapshot.child("name").value.toString()
                number.text = snapshot.child("number").value.toString()
                address.text = snapshot.child("address").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
		//회원 Uid 리턴
    private fun getCurrentUserId(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인이 되어있지 않습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        return auth.currentUser?.uid.orEmpty()
    }
}
```
