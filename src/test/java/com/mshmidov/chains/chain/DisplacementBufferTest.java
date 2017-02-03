package com.mshmidov.chains.chain;


import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static java.lang.Math.max;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("DisplacementBuffer") class DisplacementBufferTest {

    @TestFactory
    @DisplayName("Should correctly displace data")
    public List<DynamicTest> shouldCorrectlyDisplaceData() throws Exception {

        final List<DynamicTest> cases = new ArrayList<>();
        final ImmutableList<Character> data = ImmutableList.of('0', '1', '2', '3', '4', '5', '6', '7');

        for (int bufferSize = 1; bufferSize <= 7; bufferSize++) {
            for (int dataLength = 1; dataLength <= data.size(); dataLength++) {
                cases.add(createBufferTest(bufferSize, data.subList(0, dataLength)));
            }
        }

        return cases;
    }

    private <T> DynamicTest createBufferTest(int bufferSize, List<T> dataToFeed) {
        // given
        final DisplacementBuffer<T> buffer = new DisplacementBuffer<>(bufferSize);
        final List<T> expectedData = dataToFeed.subList(max(dataToFeed.size() - bufferSize, 0), dataToFeed.size());

        // when
        for (T element : dataToFeed) {
            buffer.add(element);
        }

        // then
        final String description =
                String.format("Buffer of size %s should contain %s after receiving %s as input", bufferSize, expectedData, dataToFeed);
        return dynamicTest(description, () -> assertThat(buffer.getData()).isEqualTo(expectedData.toArray()));
    }

}
