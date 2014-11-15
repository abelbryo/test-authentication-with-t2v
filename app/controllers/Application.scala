package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import jp.t2v.lab.play2.auth._
import jp.t2v.lab.play2.stackc.{ RequestWithAttributes, RequestAttributeKey, StackableController }

import scala.concurrent.{ ExecutionContext, Future }
import ExecutionContext.Implicits.global
import authentication._
import models._
import dao._

object Application extends Controller with LoginLogout with AuthConfigImpl {

  val loginForm = Form(mapping(
    "email" -> email,
    "password" -> text)(Account.authenticate)(_.map(u => (u.email, "")))
    .verifying("Invalid email or password", result => result.isDefined))

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  def logout = Action.async { implicit request =>
    Future.successful(Redirect(routes.Application.login))
  }

  def authenticate = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.login(formWithErrors)))
      },
      user => gotoLoginSucceeded(user.get.id))
  }

  val registerForm = Form[UserFormWrapper](mapping(
    "firstName" -> text,
    "lastName" -> text,
    "username" -> text,
    "password" -> text,
    "email" -> email,
    "phoneNumber" -> text)(UserFormWrapper.apply)(UserFormWrapper.unapply))

  def register = Action { implicit request =>
    Ok(views.html.register(registerForm))
  }

  def submitRegisteration = Action { implicit request =>
    registerForm.bindFromRequest.fold(
      error => {
        Ok("Registration failed.")
      },
      user => {
        val now = new org.joda.time.DateTime
        val permission = "NormalUser"
        val newUser = User(None, user.firstName, user.lastName, user.username, user.password, user.email, user.phoneNumber, permission,  now)
        UserDAO.saveUserInDB(newUser)
        Redirect(routes.Application.login)
      })
  }

}
