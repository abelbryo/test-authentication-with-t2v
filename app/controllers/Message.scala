package controllers

import play.api._
import play.api.mvc._

import jp.t2v.lab.play2.auth._
import jp.t2v.lab.play2.stackc.{RequestWithAttributes, RequestAttributeKey, StackableController}
import authentication._


object Message extends Controller with AuthElement with AuthConfigImpl {

  def main = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    println(user)
    Ok(views.html.index(s"Your new application is ready.${user}"))
  }

  // This page is only meant for admins
  def list =  StackAction(AuthorityKey -> Administrator){ implicit request =>
    val user = loggedIn
    val title = "All Messages- This page is only meant for administrators"
    Ok(views.html.index(title))
  }




}
