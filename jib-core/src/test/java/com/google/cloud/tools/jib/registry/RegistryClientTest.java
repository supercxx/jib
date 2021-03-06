/*
 * Copyright 2018 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.tools.jib.registry;

import com.google.cloud.tools.jib.JibLogger;
import com.google.cloud.tools.jib.http.Authorization;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Tests for {@link RegistryClient}. More comprehensive tests can be found in the integration tests.
 */
@RunWith(MockitoJUnitRunner.class)
public class RegistryClientTest {

  @Mock private JibLogger buildLogger;
  @Mock private Authorization mockAuthorization;

  private RegistryClient.Factory testRegistryClientFactory;

  @Before
  public void setUp() {
    testRegistryClientFactory =
        RegistryClient.factory(buildLogger, "some.server.url", "some image name");
  }

  @Test
  public void testGetUserAgent_null() {
    Assert.assertTrue(
        testRegistryClientFactory
            .setAuthorization(mockAuthorization)
            .newRegistryClient()
            .getUserAgent()
            .startsWith("jib"));

    RegistryClient.setUserAgentSuffix(null);
    Assert.assertTrue(
        testRegistryClientFactory
            .setAuthorization(mockAuthorization)
            .newRegistryClient()
            .getUserAgent()
            .startsWith("jib"));
  }

  @Test
  public void testGetUserAgent() {
    RegistryClient.setUserAgentSuffix("some user agent suffix");

    Assert.assertTrue(
        testRegistryClientFactory
            .setAllowInsecureRegistries(true)
            .newRegistryClient()
            .getUserAgent()
            .startsWith("jib "));
    Assert.assertTrue(
        testRegistryClientFactory
            .setAllowInsecureRegistries(true)
            .newRegistryClient()
            .getUserAgent()
            .endsWith(" some user agent suffix"));
  }

  @Test
  public void testGetApiRouteBase() {
    Assert.assertEquals(
        "some.server.url/v2/",
        testRegistryClientFactory
            .setAllowInsecureRegistries(true)
            .newRegistryClient()
            .getApiRouteBase());
  }
}
