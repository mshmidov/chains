package com.isabaka.chains.markov.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TrainingUnpackerTest {

    @Mock FinishedTraining<String> finishedTraining;

    @InjectMocks TrainingUnpacker<String> unpacker;

    @Test
    @DisplayName("should return order")
    void shouldReturnOrder() {
        // given
        given(finishedTraining.getOrder()).willReturn(7);

        // when
        final int order = unpacker.getOrder();

        //then
        assertThat(order).isEqualTo(7);
    }



}
