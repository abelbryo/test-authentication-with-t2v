package authentication

import play.api.mvc._
import play.api.mvc.Results._
import play.api.Play.current

import scala.concurrent.{ ExecutionContext, Future }
import ExecutionContext.Implicits.global
import jp.t2v.lab.play2.auth._
import jp.t2v.lab.play2.stackc.{ RequestWithAttributes, RequestAttributeKey, StackableController }
import reflect.classTag

import controllers._
import models._
import services._
import dao._

trait AuthConfigImpl extends AuthConfig {

  object UserService extends UserService

  type Id = UserId

  type User = Account

  type Authority = Permission

  val idTag = classTag[Id]

  val sessionTimeoutInSeconds: Int = 3600

  def resolveUser(id: Id)(implicit ctx: ExecutionContext): Future[Option[User]] = {
    Future.successful(Account.resolveUserById(id))
  }

  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) =
    Future.successful(Redirect(routes.Message.main))

  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) =
    Future.successful(Redirect(routes.Application.login))

  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) =
    Future.successful(Redirect(routes.Application.login))

  def authorizationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) =
    Future.successful(Forbidden("No Permission - Authorization Failed"))

  def authorize(account: this.User, authority: Authority)(implicit ctx: ExecutionContext): Future[Boolean] =
    Future.successful(
      (account.permission, authority) match {
        case (Administrator, _) => true
        case (NormalUser, NormalUser) => true
        case _ => false
      })

  override lazy val cookieSecureOption: Boolean = play.api.Play.isProd(play.api.Play.current)
}

case class Account(id: UserId, email: String, password: String, permission: Permission)

object Account {

  /**
   * @param id: UserId
   * @return Option[Account]
   */
  def resolveUserById(id: UserId): Option[Account] = {

    val user = UserDAO.getUserById(id)

    user match {
      case Some(u) => {
        val permission = Permission.valueOf(u.permission)
        val userAccount = Account(u.id.get, u.email, u.password, permission)
        Option[Account](userAccount)
      }
      case None => None
    }
  }

  /**
   * @param email: String
   * @param password: String
   * @return Option[Account]
   */
  def authenticate(email: String, password: String): Option[Account] = {
    val user = UserDAO.getUserByEmail(email)
    val result: Option[User] = user.filter(u => u.password == password)

    result match {
      case Some(res) => {
        val permission = Permission.valueOf(res.permission)
        val userAccount = Account(res.id.get, res.email, res.password, permission)
        Option[Account](userAccount)
      }
      case None => None
    }
  }

}

