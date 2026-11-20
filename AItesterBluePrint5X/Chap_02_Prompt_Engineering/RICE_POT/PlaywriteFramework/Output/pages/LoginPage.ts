import { type Locator, type Page, type TestInfo, test } from '@playwright/test';

export class LoginPageError extends Error {
  constructor(action: string, cause: unknown) {
    const reason = cause instanceof Error ? cause.message : String(cause);
    super(`LoginPage.${action} failed: ${reason}`, { cause });
    this.name = 'LoginPageError';
  }
}

export class LoginPage {
  static readonly PATH = '/#/login';

  readonly loginForm: Locator;
  readonly emailInput: Locator;
  readonly passwordInput: Locator;
  readonly passwordVisibilityToggle: Locator;
  readonly signInButton: Locator;
  readonly rememberMeCheckbox: Locator;
  readonly rememberMeLabel: Locator;
  readonly forgotPasswordButton: Locator;
  readonly errorMessage: Locator;
  readonly googleSignInButton: Locator;
  readonly ssoSignInButton: Locator;
  readonly freeTrialLink: Locator;
  readonly forgotPasswordForm: Locator;
  readonly forgotPasswordEmailInput: Locator;
  readonly forgotPasswordBackButton: Locator;
  readonly forgotPasswordResetButton: Locator;

  constructor(private readonly page: Page) {
    this.loginForm = page.locator('#js-login-form');
    this.emailInput = page.locator('#login-username');
    this.passwordInput = page.locator('#login-password');
    this.passwordVisibilityToggle = this.loginForm.getByRole('button', { name: 'Toggle password visibility' });
    this.signInButton = page.locator('#js-login-btn');
    this.rememberMeCheckbox = page.locator('#checkbox-remember');
    this.rememberMeLabel = this.loginForm.getByText('Remember me', { exact: true });
    this.forgotPasswordButton = page.getByRole('button', { name: 'Forgot Password?' });
    this.errorMessage = page.locator('#js-notification-box-msg');
    this.googleSignInButton = page.locator('#js-google-signin-btn');
    this.ssoSignInButton = page.getByRole('button', { name: /sign in using sso/i });
    this.freeTrialLink = page.getByRole('link', { name: /start a free trial/i });
    this.forgotPasswordForm = page.locator('#js-forgot-password-form');
    this.forgotPasswordEmailInput = page.locator('#forgot-password-username');
    this.forgotPasswordBackButton = this.forgotPasswordForm.getByRole('button', { name: /back/i });
    this.forgotPasswordResetButton = this.forgotPasswordForm.getByRole('button', { name: 'Reset Password' });
  }

  async open(): Promise<void> {
    await this.perform('open', 'Open the login page', async () => {
      await this.page.goto(LoginPage.PATH);
      await this.emailInput.waitFor({ state: 'visible' });
    });
  }

  async enterEmail(email: string): Promise<void> {
    await this.perform('enterEmail', `Enter email "${email}"`, () => this.emailInput.fill(email));
  }

  async enterPassword(password: string): Promise<void> {
    await this.perform('enterPassword', 'Enter password', () => this.passwordInput.fill(password));
  }

  async clickSignIn(): Promise<void> {
    await this.perform('clickSignIn', 'Click "Sign in"', () => this.signInButton.click());
  }

  async login(email: string, password: string): Promise<void> {
    await this.perform('login', `Sign in as "${email || '<empty>'}"`, async () => {
      await this.enterEmail(email);
      await this.enterPassword(password);
      await this.clickSignIn();
    });
  }

  async togglePasswordVisibility(): Promise<void> {
    await this.perform('togglePasswordVisibility', 'Toggle password visibility', () =>
      this.passwordVisibilityToggle.click(),
    );
  }

  async toggleRememberMe(): Promise<void> {
    await this.perform('toggleRememberMe', 'Click the "Remember me" label', () => this.rememberMeLabel.click());
  }

  async isRememberMeChecked(): Promise<boolean> {
    return this.perform('isRememberMeChecked', 'Read the "Remember me" state', () =>
      this.rememberMeCheckbox.isChecked(),
    );
  }

  async openForgotPassword(): Promise<void> {
    await this.perform('openForgotPassword', 'Open "Forgot Password?"', () => this.forgotPasswordButton.click());
  }

  async returnFromForgotPassword(): Promise<void> {
    await this.perform('returnFromForgotPassword', 'Click "Back" on the forgot-password form', () =>
      this.forgotPasswordBackButton.click(),
    );
  }

  async attachFailureEvidence(testInfo: TestInfo): Promise<void> {
    await this.perform('attachFailureEvidence', 'Attach failure evidence', async () => {
      await testInfo.attach('failure-url', { body: this.page.url(), contentType: 'text/plain' });
      await testInfo.attach('failure-screenshot', {
        body: await this.page.screenshot({ fullPage: true }),
        contentType: 'image/png',
      });
    });
  }

  private async perform<T>(action: string, title: string, body: () => Promise<T>): Promise<T> {
    return test.step(title, async () => {
      try {
        return await body();
      } catch (error) {
        if (error instanceof LoginPageError) {
          throw error;
        }
        throw new LoginPageError(action, error);
      }
    });
  }
}
