package controllers

import actors.msg.{ReadNextMsg, WriteMsg, ReadAtMsg}
import actors.{HistoryActor}
import akka.actor.{ActorSystem, Props}
import play.api.mvc._
import scala.concurrent.duration.Duration

import play.api.Play.current // brings the current running Application into context

object Application extends Controller {


// TODO: WS
//  def socket = WebSocket.acceptWithActor[JsValue, JsValue] { request => out =>
//    MyWebSocketActor.props(out)
//  }

  // TODO: will be WS, just testing for now
  def index = Action {

    val system = ActorSystem("historySystem")

    //  create top-level actors
    val historyActor = system.actorOf(
                          Props(classOf[HistoryActor], "history-file.log"),
                          name = "history-actor"
                        )

    // just to to feel that it works / send msg and forwards it properly (see the logs)
    // then we should provide with proper testings

    historyActor ! WriteMsg("line 1")
    historyActor ! WriteMsg("line 2")
    historyActor ! WriteMsg("line 3")

    historyActor ! ReadNextMsg
    historyActor ! ReadNextMsg
    historyActor ! ReadNextMsg

    Ok(views.html.index("History Demo"))
  }

}