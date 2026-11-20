import { expect, test } from '@playwright/test';
import * as allure from 'allure-js-commons';
import { LoginPage } from '../pages/LoginPage';

const PAGE_TITLE = 'Login - Wingify';
const INSUFFICIENT_INFORMATION = 'Insufficient information to determine.';

test.use({ trace: 'off' });

test.describe('VWO Login - valid scenarios', () => {
  let loginPage: LoginPage;

  test.beforeEach(async ({ page }) => {
    await allure.epic('VWO');
    await allure.feature('Login');
    await allure.story('Valid login scenarios');
    loginPage = new LoginPage(page);
    await loginPage.open();
  });

  test.afterEach(async ({}, testInfo) => {
    if (testInfo.status !== testInfo.expectedStatus) {
      await loginPage.attachFailureEvidence(testInfo);
    }
  });

  test('TC_V01 Login page renders', async ({ page }) => {
    await expect(page).toHaveTitle(PAGE_TITLE);
    await expect(loginPage.emailInput).toBeVisible();
    await expect(loginPage.passwordInput).toBeVisible();
    await expect(loginPage.signInButton).toBeVisible();
    await expect(loginPage.rememberMeLabel).toBeVisible();
    await expect(loginPage.forgotPasswordButton).toBeVisible();
    await expect(loginPage.freeTrialLink).toBeVisible();
  });

  test('TC_V02 Email field is auto-focused', async () => {
    await expect(loginPage.emailInput).toBeFocused();
  });

  test('TC_V03 Password is masked and can be revealed', async () => {
    await expect(loginPage.passwordInput).toHaveAttribute('type', 'password');
    await loginPage.togglePasswordVisibility();
    await expect(loginPage.passwordInput).toHaveAttribute('type', 'text');
    await loginPage.togglePasswordVisibility();
    await expect(loginPage.passwordInput).toHaveAttribute('type', 'password');
  });

  test('TC_V04 Remember me toggles via its label', async () => {
    const initialState = await loginPage.isRememberMeChecked();
    await loginPage.toggleRememberMe();
    await expect(loginPage.rememberMeCheckbox).toBeChecked({ checked: !initialState });
    await loginPage.toggleRememberMe();
    await expect(loginPage.rememberMeCheckbox).toBeChecked({ checked: initialState });
  });

  test('TC_V05 Forgot Password view opens and Back returns', async () => {
    await loginPage.openForgotPassword();
    await expect(loginPage.forgotPasswordForm).toBeVisible();
    await expect(loginPage.forgotPasswordEmailInput).toBeVisible();
    await expect(loginPage.forgotPasswordResetButton).toBeVisible();
    await loginPage.returnFromForgotPassword();
    await expect(loginPage.loginForm).toBeVisible();
    await expect(loginPage.forgotPasswordForm).toBeHidden();
  });

  test('TC_V06 Enterprise and social sign-in options are offered', async () => {
    await expect(loginPage.googleSignInButton).toBeVisible();
    await expect(loginPage.ssoSignInButton).toBeVisible();
  });

  test('TC_V07 Valid credentials sign in', async () => {
    const username = process.env.VALID_USERNAME?.trim() ?? '';
    const password = process.env.VALID_PASSWORD?.trim() ?? '';
    test.skip(username === '' || password === '', INSUFFICIENT_INFORMATION);
    await loginPage.login(username, password);
    await expect(loginPage.loginForm).toBeHidden();
    await expect(loginPage.errorMessage).toBeHidden();
  });
});
