package controllers

import play.api.libs.EventSource
import actors.msg.{ReadNextMsg, WriteMsg}
import actors.{HistoryActor}
import akka.actor.{ActorSystem, Props}
import play.api.libs.iteratee.{Enumerator, Concurrent}
import play.api.libs.json.{Json,JsValue}
import play.api.mvc._

import play.api.Play.current   // brings the current running Application into context

object Application extends Controller {

  val system = ActorSystem("historySystem")

  //  create top-level actors
  val historyActor = system.actorOf(
    Props(classOf[HistoryActor], "history-file.log"),
    name = "history-actor"
  )

  val (out, channel) = Concurrent.broadcast[JsValue]

  def feed = Action { req =>
    println("Someone connected")
    Ok.chunked(welcome >>> out &> EventSource()).as("text/event-stream")
  }

  def postMessage = Action(parse.json) { req => {

    historyActor ! WriteMsg( req.body.toString() )      // send a message-info to the HistoryActor /lib, to store

//    historyActor ! ReadNextMsg

    channel.push(req.body)

    Ok
  }}

  def welcome = Enumerator.apply[JsValue](Json.obj(
    "user" -> "Bot",
    "message" -> "Welcome! Enter a message and hit Enter"
  ))

  def index = Action { implicit req =>
    Ok(views.html.index(routes.Application.feed(), routes.Application.postMessage()))
  }

}