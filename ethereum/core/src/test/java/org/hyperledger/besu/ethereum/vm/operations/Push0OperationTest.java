/*
 * Copyright contributors to Hyperledger Besu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.besu.ethereum.vm.operations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hyperledger.besu.evm.Code.EMPTY_CODE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.gascalculator.BerlinGasCalculator;
import org.hyperledger.besu.evm.gascalculator.GasCalculator;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.evm.operation.Operation.OperationResult;
import org.hyperledger.besu.evm.operation.Push0Operation;
import org.hyperledger.besu.evm.operation.PushOperation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class Push0OperationTest {

  private final GasCalculator gasCalculator = new BerlinGasCalculator();

  @Test
  public void shouldPush0OntoStack() {
    final MessageFrame frame = createMessageFrame(100, Optional.of(Wei.of(5L)));
    final Operation operation = new Push0Operation(gasCalculator);
    final OperationResult result = operation.execute(frame, null);
    Mockito.verify(frame).pushStackItem(Bytes.EMPTY);
    assertThat(result.getGasCost()).isEqualTo(gasCalculator.getBaseTierGasCost());
    assertSuccessResult(result);
  }
  private void assertSuccessResult(final OperationResult result) {
    assertThat(result).isNotNull();
    assertThat(result.getHaltReason()).isNull();
  }
  private MessageFrame createMessageFrame(final long initialGas, final Optional<Wei> baseFee) {
    final MessageFrame frame = mock(MessageFrame.class);
    when(frame.getRemainingGas()).thenReturn(initialGas);
    final BlockHeader blockHeader = mock(BlockHeader.class);
    when(blockHeader.getBaseFee()).thenReturn(baseFee);
    when(frame.getBlockValues()).thenReturn(blockHeader);
    when(frame.getCode()).thenReturn(EMPTY_CODE);
    return frame;
  }
}
