package models

case class Question(
		val spotifyUri: String,
		val qType: String,
		val alternatives: List[String],
		val correctAnswer: Int
		) 

case class Quiz(
		// created | started | finished
		val status: String,
		val players: List[String],
		val questions: List[Question],
		val answers: Map[String,List[(Int, Int)]],
		val updates: Map[String,List[(Int, Int)]]
		)
		
object Quiz {
  def makeQuiz(): Quiz = {
    Quiz("created", Nil, Nil, Map.empty, Map.empty)
    
  }
  def addQuestion(quiz: Quiz, spotifyUri: String, qType: String,alternatives: List[String], correctAnswer: Int): Quiz = {
    Quiz(quiz.status,
    	 quiz.players, 
         Question(spotifyUri, qType, alternatives, correctAnswer) :: quiz.questions, 
         quiz.answers, //TODO: remember to add -1 for persons
         quiz.updates)
  }
  
  def joinQuiz(quiz: Quiz, player: String): Quiz = {
    val cleanAnswers = List.range(0,quiz.questions.length).map(x=>(x,-1))
    val playerAnswers = quiz.answers + (player->cleanAnswers)
    Quiz(quiz.status, quiz.players ::: List(player), quiz.questions, playerAnswers, quiz.updates )
  }
  
  def emptyUpdates(quiz: Quiz): Quiz = {
    Quiz(quiz.status, quiz.players, quiz.questions, quiz.answers, Map.empty)
  }
  
  def updateQuiz(quiz: Quiz, player: String, questionIndex: Int, answer: Int) = {
    val playerAnswers = quiz.answers.get(player)
    
    playerAnswers match {
      case Some(playerAnswers) => 
      	val updatedAnswers = playerAnswers.updated(questionIndex, (questionIndex, answer))

      	val updates = quiz.updates.get(player)
      	updates match {
      	  case Some(updates) =>
      	    val playerUpdates = (questionIndex, answer) :: updates
      	    Quiz(quiz.status, quiz.players, quiz.questions, quiz.answers + (player->updatedAnswers), quiz.updates + (player->playerUpdates))
      	  case None => 
      	    Quiz(quiz.status, quiz.players, quiz.questions, quiz.answers + (player->updatedAnswers), quiz.updates + (player->List((questionIndex,answer))) )
      	}
      case None => quiz
    }
  }
  
  def setStatus(quiz: Quiz, status: String) = {
      Quiz(status, quiz.players, quiz.questions, quiz.answers, quiz.updates)
  }
	
}