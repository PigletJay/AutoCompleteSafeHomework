package utils

object Urls {

    //用户登录
    const val userLogin = "https://appapi.safetree.com.cn/usercenter/api/v1/account/PostLogin"

    //服务端
    const val serviceSide = "https://huodongapi.safetree.com.cn/Topic/topic/main/api/v1/users/get-serviceside?"

    //作业列表
    const val homeworkList = "{__weburl__}safeapph5/api/v1/homework/homeworklist"

    //技能问题列表
    const val skillQuestionList = "{__weburl__}PhoneEpt/SkillQuestionList.aspx?"

    //专题活动签到
    const val specialSign = "{__serviceurl__}Topic/topic/platformapi/api/v1/records/sign"

    //假期作业签到
    const val holidaySign = "{__serviceurl__}Topic/topic/platformapi/api/v1/holiday/sign"

    //知识测试
    const val knowledgeTest = "{__weburl__}CommonService.asmx/TemplateIn2"

    //家校互动
    const val schoolHomeInteraction = "{__weburl__}CommonService.asmx/Skill_ThirdRecord"
}