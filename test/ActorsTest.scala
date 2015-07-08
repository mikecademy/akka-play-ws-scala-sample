import actors.{HistoryActor, WriterActor}
import actors.msg.{WriteResult, WriteMsg}

import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

import akka.actor.ActorSystem

import scala.concurrent.duration._

import akka.testkit._

/**
 * Fortunately, Akka Testkit comes with the TestActorRef class.
 * When you create an Actor with the TestActorRef, it will use a special message dispatcher called
 * CallingThreadDispatcher that will execute the actor receive() method in the same thread it has been sent.
 * Therefore, all your tell() calls will become synchronous and more easily testable.
 *
 * In addition to making the actor message processing synchronous, the TestActorRef will give back a special
 * ActorRef instance that allows you to access the underlying actor instance
 * (which is not possible with standard Akka actors).
 * It allows you to make assertions about some of the actor’s internal states.
 */
class ActorsTest
  extends TestKit(ActorSystem("historySystem"))
  with DefaultTimeout with ImplicitSender
  with WordSpecLike with org.scalatest.MustMatchers with BeforeAndAfterAll {

  "A Writer Actor" must {

    // given
    val actorRef = TestActorRef(new WriterActor("history-file.log"))

    "reply with a feedback and change/increment the state" in {

      // when (1)
      actorRef ! WriteMsg("line 1")           // first msg

      // then (1.1)
      expectMsg( WriteResult(1, 7) )

      // then (1.2) - state has been changed
      actorRef.underlyingActor.lines must equal(1)
      actorRef.underlyingActor.chars must equal(7)

      // when (2)
      actorRef ! WriteMsg("line 2")           // second msg

      // then (2.1)
      expectMsg( WriteResult(2, 14) )

      // then (2.2) - state has been changed/incremented
      actorRef.underlyingActor.lines must equal(2)
      actorRef.underlyingActor.chars must equal(14)

    }

  }


  // TODO:
  "A History Actor" must {

//    // given
      val historyActorRef = TestActorRef(new HistoryActor("history-file.log"))       // Creation of the TestActorRef


//      "resend WriteMessage to WriterActor" in {
//        // TODO:
//      }


    val writerActorRef = TestActorRef(new WriterActor("history-file.log"))       // Creation of the TestActorRef

    historyActorRef.underlyingActor.writerActor = writerActorRef

    "receive messages and change state" in {  // integration-like test (http://stackoverflow.com/questions/31298266/akka-testkit-how-to-wait-for-the-message-to-be-processed)

      // This call is synchronous. The actor receive() method will be called in the current thread

      // when
      historyActorRef ! WriteMsg("line 1")

      // then (1) - got WriteResult (from WriterActor as result of getting WriteMsg)
      within(200 millis) {
        //expectMsg(WriteResult(1, 7))
      }

      // then (2) - state
      //historyActorRef.underlyingActor.lastWrite must equal(WriteResult(1,7)) // With actorRef.underlyingActor, we can access the react actor instance created by Akka
    }
  }

}
