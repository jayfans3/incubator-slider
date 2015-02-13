/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.slider.api.proto;

import org.apache.slider.api.types.ApplicationLivenessInformation;
import org.apache.slider.api.types.ComponentInformation;
import org.apache.slider.api.types.ContainerInformation;
import org.apache.slider.core.conf.AggregateConf;
import org.apache.slider.core.conf.ConfTree;
import org.apache.slider.core.conf.ConfTreeOperations;
import org.apache.slider.core.persist.AggregateConfSerDeser;
import org.apache.slider.core.persist.ConfTreeSerDeser;
import org.codehaus.jackson.JsonParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Class to handle marshalling of REST
 * types to/from Protobuf records.
 */
public class RestTypeMarshalling {

  public static Messages.ApplicationLivenessInformationProto
  marshall(ApplicationLivenessInformation info) {

    Messages.ApplicationLivenessInformationProto.Builder builder =
        Messages.ApplicationLivenessInformationProto.newBuilder();
    builder.setAllRequestsSatisfied(info.allRequestsSatisfied);
    builder.setRequestsOutstanding(info.requestsOutstanding);
    return builder.build();
  }

  public static ApplicationLivenessInformation
  unmarshall(Messages.ApplicationLivenessInformationProto wire) {
    ApplicationLivenessInformation info = new ApplicationLivenessInformation();
    info.allRequestsSatisfied = wire.getAllRequestsSatisfied();
    info.requestsOutstanding = wire.getRequestsOutstanding();
    return info;
  }

  public static ComponentInformation
  unmarshall(Messages.ComponentInformationProto wire) {
    ComponentInformation info = new ComponentInformation();
    info.name = wire.getName();
    info.priority = wire.getPriority();
    info.placementPolicy = wire.getPlacementPolicy();
    
    info.actual = wire.getActual();
    info.completed = wire.getCompleted();
    info.desired = wire.getDesired();
    info.failed = wire.getFailed();
    info.releasing = wire.getReleasing();
    info.requested = wire.getRequested();
    info.started = wire.getStarted();
    info.startFailed = wire.getStartFailed();
    info.totalRequested = wire.getTotalRequested();
    info.containers = new ArrayList<String>(wire.getContainersList());
    if (wire.hasFailureMessage()) {
      info.failureMessage = wire.getFailureMessage();
    }

    return info;
  }


  public static Messages.ComponentInformationProto
  marshall(ComponentInformation info) {

    Messages.ComponentInformationProto.Builder builder =
        Messages.ComponentInformationProto.newBuilder();
    builder.setName(info.name);
    builder.setPriority(info.priority);
    builder.setPlacementPolicy(info.placementPolicy);
    
    builder.setActual(info.actual);
    builder.setCompleted(info.completed);
    builder.setDesired(info.desired);
    builder.setFailed(info.failed);
    builder.setReleasing(info.releasing);
    builder.setRequested(info.requested);
    builder.setStarted(info.started);
    builder.setStartFailed(info.startFailed);
    builder.setTotalRequested(info.totalRequested);
    if (info.failureMessage != null) {
      builder.setFailureMessage(info.failureMessage);
    }
    if (info.containers != null) {
      builder.addAllContainers(info.containers);
    }
    return builder.build();
  }

  public static ContainerInformation
  unmarshall(Messages.ContainerInformationProto wire) {
    ContainerInformation info = new ContainerInformation();
    info.containerId = wire.getContainerId();
    info.component = wire.getComponent();
    info.state = wire.getState();
    if (wire.hasReleased()) {
      info.released = wire.getReleased();
    }
    if (wire.hasExitCode()) {
      info.exitCode = wire.getExitCode();
    }
    if (wire.hasDiagnostics()) {
      info.diagnostics = wire.getDiagnostics();
    }
    if (wire.hasHost()) {
      info.host = wire.getHost();
    }
    if (wire.hasHostURL()) {
      info.host = wire.getHostURL();
    }
    info.createTime = wire.getCreateTime();
    info.startTime = wire.getStartTime();
    info.output = wire.getOutputList().toArray(
        new String[wire.getOutputCount()]
    );
    return info;
  }

  public static Messages.ContainerInformationProto
     marshall(ContainerInformation info) {

    Messages.ContainerInformationProto.Builder builder =
        Messages.ContainerInformationProto.newBuilder();
    if (info.containerId != null) {
      builder.setContainerId(info.containerId);
    }
    if (info.component != null) {
      builder.setComponent(info.component);
    }
    if (info.diagnostics != null) {
      builder.setDiagnostics(info.diagnostics);
    }
    if (info.host != null) {
      builder.setHost(info.host);
    }
    if (info.hostURL != null) {
      builder.setHostURL(info.hostURL);
    }
    if (info.released != null) {
      builder.setReleased(info.released);
    }
    if (info.output != null) {
      builder.addAllOutput(Arrays.asList(info.output));
    }
    builder.setCreateTime(info.createTime);
    builder.setStartTime(info.startTime);
    return builder.build();
  }

  public static String
    unmarshall(Messages.WrappedJsonProto wire) {
    return wire.getJson();
  }

  public static ConfTree unmarshallToConfTree(Messages.WrappedJsonProto wire) throws
      IOException {
    return new ConfTreeSerDeser().fromJson(wire.getJson());
  }
  
  public static ConfTreeOperations unmarshallToCTO(Messages.WrappedJsonProto wire) throws
      IOException {
    return new ConfTreeOperations(new ConfTreeSerDeser().fromJson(wire.getJson()));
  }

  public static AggregateConf unmarshallToAggregateConf(Messages.WrappedJsonProto wire) throws
      IOException {
    return new AggregateConfSerDeser().fromJson(wire.getJson());
  }

}
