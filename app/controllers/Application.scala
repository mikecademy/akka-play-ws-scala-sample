package controllers

import actors.msg.{WriteMsg, ReadMsg}
import actors.{HistoryActor}
import akka.actor.{ActorSystem, Props}
import play.api.mvc._

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
//    historyActor ! WriteMsg("line 2")
//    historyActor ! WriteMsg("line 3")

    historyActor ! ReadMsg(0)
    historyActor ! ReadMsg(1)
    historyActor ! ReadMsg(2)

    Ok(views.html.index("History Demo"))
  }

}