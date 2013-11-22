package com.echo8.bprmf.data;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.echo8.bprmf.type.UserItemPair;
import com.echo8.bprmf.utils.IdGenerator;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackDataTest {
    @Mock
    private IdGenerator userIdGenerator;

    @Mock
    private IdGenerator itemIdGenerator;

    @InjectMocks
    private FeedbackData feedbackData;

    @Test
    public void testGetItemIdSetForUserId() {
        when(userIdGenerator.getId("user1")).thenReturn(1);
        when(itemIdGenerator.getId("item1")).thenReturn(2);

        feedbackData.addFeedback(new UserItemPair("user1", "item1"));

        Set<Integer> result = new HashSet<Integer>(Arrays.asList(2));
        assertThat(feedbackData.getItemIdSetForUserId(1), is(result));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetItemIdSetForUserIdWithInvalidUserId() {
        feedbackData.getItemIdSetForUserId(5);
    }
}
