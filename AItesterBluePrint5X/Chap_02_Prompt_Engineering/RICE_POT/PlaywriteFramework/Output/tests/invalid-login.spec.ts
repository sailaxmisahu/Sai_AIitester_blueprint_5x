import { expect, test } from '@playwright/test';
import * as allure from 'allure-js-commons';
import { LoginPage } from '../pages/LoginPage';

const FAILED_LOGIN_MESSAGE = 'Your email, password, IP address or location did not match';
const LOGIN_URL_PATTERN = /#\/login$/;
const UNREGISTERED_EMAIL = 'qa.unregistered.user@example.com';
const INVALID_PASSWORD = 'Invalid@Pass#2026';

interface InvalidLoginCase {
  readonly id: string;
  readonly scenario: string;
  readonly email: string;
  readonly password: string;
}

const INVALID_LOGIN_CASES: readonly InvalidLoginCase[] = [
  { id: 'TC_I01', scenario: 'Empty email and password', email: '', password: '' },
  { id: 'TC_I02', scenario: 'Malformed email', email: 'not-an-email', password: INVALID_PASSWORD },
  { id: 'TC_I03', scenario: 'Unregistered email with empty password', email: UNREGISTERED_EMAIL, password: '' },
  { id: 'TC_I04', scenario: 'Unregistered email with wrong password', email: UNREGISTERED_EMAIL, password: INVALID_PASSWORD },
];

test.describe('VWO Login - invalid scenarios', () => {
  let loginPage: LoginPage;

  test.beforeEach(async ({ page }) => {
    await allure.epic('VWO');
    await allure.feature('Login');
    await allure.story('Invalid login scenarios');
    loginPage = new LoginPage(page);
    await loginPage.open();
  });

  test.afterEach(async ({}, testInfo) => {
    if (testInfo.status !== testInfo.expectedStatus) {
      await loginPage.attachFailureEvidence(testInfo);
    }
  });

  for (const loginCase of INVALID_LOGIN_CASES) {
    test(`${loginCase.id} ${loginCase.scenario} is rejected`, async ({ page }) => {
      await loginPage.login(loginCase.email, loginCase.password);
      await expect(loginPage.errorMessage).toHaveText(FAILED_LOGIN_MESSAGE);
      await expect(page).toHaveURL(LOGIN_URL_PATTERN);
    });
  }
});
