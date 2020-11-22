package utils

import model.*

object ApiService {

    //用户信息
    var userInfo: UserInfo? = null

    /**
     * 尝试登录
     */
    suspend fun doLogin(username: String, password: String = "123456"): Boolean = runCatching {
        val loginResult = HttpClient.post<LoginResult>(Urls.userLogin, LoginParams(username, password))
        if (loginResult.errCode == 0) {
            userInfo = loginResult.data!!
            //设置服务网址
            userInfo!!.serviceUrl = "https:${getServiceSide()}/"
        } else {
            throw Error(loginResult.errDesc)
        }
    }.onSuccess {
        println("$username 登录成功")
    }.onFailure {
        println("$username 登录失败[${it.message}]")
    }.isSuccess

    /**
     * 尝试完成作业
     */
    suspend fun doHomework(homework: Homework) = runCatching {
        when (homework.sort) {
            "Skill" -> doSkill(homework.url)
            "Special" -> doSpecial(homework.url)
            "SummerWinterHoliday" -> doHoliday(homework.publishDateTime.take(4).toInt(), homework.semester)
        }
    }.onSuccess {
        println("Success")
    }.onFailure {
        println("Failure[${it.message}]")
    }.isSuccess

    /**
     * 获取未完成作业列表
     */
    suspend fun homeworkList(): List<Homework> {
        return HttpClient.get<List<Homework>>(Urls.homeworkList).filter { it.workStatus == "UnFinish" }
    }

    /**
     * 尝试完成安全技能
     */
    private suspend fun doSkill(url: String) {
        val params = url
            .substringAfter("?")
            .split("&")
            .associateBy({ it.substringBefore("=") }, { it.substringAfter("=") })
        //获取视频标识及Url参数信息
        val vid = HttpClient.get<String>(url).substringAfter("JXHD_vid = \"").substringBefore("\"")
        val cid = params["li"]
        val gid = params["gid"]
        //获取技能问题列表
        val skillQuestionList = getSkillQuestionList(cid!!)
        val fid = skillQuestionList.substringAfter("fid:").substringBefore(",")
        val wid = skillQuestionList.substringAfter("workid:").substringBefore(",")
        //完成知识测试
        doKnowledgeTest(fid, wid, cid)
        //完成家校互动
        doSchoolHomeInteraction(gid!!, vid, cid)
    }

    /**
     * 尝试完成专题活动
     */
    private suspend fun doSpecial(url: String) {
        val specialId = getSpecialId(url)
        repeat(3) { step ->
            HttpClient.post<SpecialResult>(Urls.specialSign, SpecialParams(step + 1, specialId))
        }
    }

    /**
     * 尝试完成假期作业
     */
    private suspend fun doHoliday(schoolYear: Int, semester: Int) {
        repeat(3) { step ->
            HttpClient.post<HolidayResult>(Urls.holidaySign, HolidayParams(step + 1, semester, schoolYear))
        }
    }

    /**
     * 尝试完成知识测试
     */
    private suspend fun doKnowledgeTest(fid: String, wid: String, cid: String) {
        HttpClient.post<String>(Urls.knowledgeTest, KnowledgeTestParams(fid, wid, cid))
    }

    /**
     * 尝试完成家校互动
     */
    private suspend fun doSchoolHomeInteraction(gid: String, vid: String, cid: String) {
        HttpClient.post<SchoolHomeInteractionResult>(
            Urls.schoolHomeInteraction,
            SchoolHomeInteractionParams(
                gid, vid, cid,
                userInfo!!.accessCookie, userInfo!!.cityId.toString(), userInfo!!.comefrom.toString()
            )
        )
    }

    /**
     * 获取服务端
     */
    private suspend fun getServiceSide(): String {
        return HttpClient.get<String>(Urls.serviceSide, "serviceSide=${userInfo!!.webUrl}").replace("\"", "")
    }

    /**
     * 获取技能问题列表
     */
    private suspend fun getSkillQuestionList(cid: String): String {
        return HttpClient.get(Urls.skillQuestionList, "course=$cid")
    }

    /**
     * 获取专题活动标识
     */
    private suspend fun getSpecialId(url: String): String = with(HttpClient.get<String>(url)) {
        val urlFile = substringAfter("region == 0", "").substringBefore(";")
            .substringAfter("'").substringBeforeLast("'")
        if (urlFile.isNotEmpty()) {
            val urlPath = url.substringBeforeLast("/")
            return getSpecialId("$urlPath/$urlFile")
        }
        return substringAfter("data-specialId").substringAfter("\"").substringBefore("\"")
    }
}