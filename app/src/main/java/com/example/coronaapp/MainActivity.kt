package com.example.coronaapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_main.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {


    var data:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MY_LOG","1")

    }

    fun Onclick(v : View){

        textView.setText("로딩중...")
        if(edit.text.toString() == ""){
            textView.setText("날짜를 입력하세요")
            return
        }
        if(edit.text.toString().length != 8){
            textView.setText("형식이 잘못되었습니다")
            return
        }

        thread(start = true){
            data = getxmlData()
            runOnUiThread{
                Log.d("MY_LOG","2")

                Log.d("MY_LOG","3")
                textView.setText(data)
            }

        }



    }

    fun getxmlData():String{
        var buffer:StringBuffer = StringBuffer()
        var str:String = edit.text.toString()
        var location:String = java.net.URLEncoder.encode(str)
        var queryUrl:String = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey=sdFEdKUZIn0OBbfvA3PYr4uJo%2Bj9DCLHC%2FrU0OYdMTQ5iIPM6aZma78cQMYKZ6E4h095tZG1M4ayiks%2BweWeuw%3D%3D&pageNo=1&numOfRows=10&startCreateDt=" + str +"&endCreateDt="+str

        //Toast.makeText(applicationContext, queryUrl, Toast.LENGTH_SHORT).show()
        try{
            val url:URL = URL(queryUrl)
            val IS:InputStream = url.openStream()

            var factory:XmlPullParserFactory = XmlPullParserFactory.newInstance()
            var xpp:XmlPullParser = factory.newPullParser()
            xpp.setInput(InputStreamReader(IS,"UTF-8"))

            var tag:String?=null

            xpp.next()
            var eventType = xpp.eventType
            while(eventType != XmlPullParser.END_DOCUMENT){
                Log.d("MY_LOG","!!")
                when(eventType){
                    XmlPullParser.START_DOCUMENT ->{
                        buffer.append("파싱 시작\n\n")
                    }

                    XmlPullParser.START_TAG ->{
                        tag = xpp.name
                        if(tag.equals("item")){

                        }
                        else if(tag.equals("gubun")){
                            buffer.append("지역 : ")
                            xpp.next()
                            buffer.append(xpp.text)
                            buffer.append("\n")
                        }
                        else if(tag.equals("incDec")){
                            buffer.append("추가 확진자 : ")
                            xpp.next()
                            buffer.append(xpp.text)
                            buffer.append("\n")
                        }
                        else if(tag.equals("isolClearCnt")){
                            buffer.append("격리해제 : ")
                            xpp.next()
                            buffer.append(xpp.text)
                            buffer.append("\n")
                        }
                        else if(tag.equals("defCnt")){
                            buffer.append("총 확진자 : ")
                            xpp.next()
                            buffer.append(xpp.text)
                            buffer.append("\n")
                        }
                        else if(tag.equals("deathCnt")){
                            buffer.append("총 사망자 : ")
                            xpp.next()
                            buffer.append(xpp.text)
                            buffer.append("\n")
                        }
                    }


                    XmlPullParser.TEXT->{}
                    XmlPullParser.END_TAG->{
                        tag = xpp.name
                        if(tag.equals("item")){
                            buffer.append("\n\n")
                        }
                    }

                }
                eventType = xpp.next()

            }
        } catch(e:Exception){
            Log.d("MY_LOG",e.toString())
        }

        buffer.append("파싱 끝\n")
        return buffer.toString()

    }
}