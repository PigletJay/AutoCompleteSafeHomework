import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import utils.ApiService
import java.io.File
import java.util.*

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            println("自动完成安全作业")
            println("本程序使用Kotlin编写，开源地址：https://github.com/PigletJay/AutoCompleteSafeHomework\n")
            println("请在打开文件内输入学生账号（每行一个）并保存")
            println("保存完成后，请按下回车键运行...")
            val file = File("students.txt")
            file.takeUnless { it.exists() }?.createNewFile()
            //调用记事本
            invokeNotepad(file.path)
            //等待按下空格
            Scanner(System.`in`).use { it.nextLine() }
            //读取学生账号
            val students = file.readLines().filter { it.isNotEmpty() }

            println("检测到 ${students.size} 个学生账号")
            println("开始运行...\n")

            for (stuAccount in students) {
                if (ApiService.doLogin(stuAccount)) {
                    //获取未完成作业列表
                    val homeworkList = ApiService.homeworkList()
                    //检测是否有未完成作业
                    if (!homeworkList.isNullOrEmpty()) {
                        println("检测到 ${homeworkList.size} 个作业未完成")
                        //请求数据
                        homeworkList.map { async(IO) { ApiService.doHomework(it) } }.awaitAll()

                        println("请求提交完成")
                    } else {
                        println("没有检测到未完成的作业")
                    }
                }
                println()
            }

            println("运行完成，感谢使用！")
        }

        /**
         * 调用记事本
         */
        private fun invokeNotepad(filePath: String) = try {
            Runtime.getRuntime().exec("notepad.exe $filePath")
        } catch (err: Throwable) {
            println("无法为您打开文件，请到运行目录打开 students.txt 并按上述步骤操作")
        }
    }
}