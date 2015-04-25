package com.molloc.app.guava;

import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.molloc.app.BaseTest;

public class TestMultimap extends BaseTest
{
	Map<String, List<StudentScore>> studentScoreMap = Maps.newHashMap();

	@Test
	public void testStudentScore()
	{
		for (int i = 10; i < 20; i++)
		{
			StudentScore studentScore = new StudentScore();
			studentScore.courseId = 1001 + i;
			studentScore.score = 100 - i;
			addStudentScore("lcyan", studentScore);
		}
		logger.info("StudentScoreMap: {}", studentScoreMap.size());
		logger.info("StudentScoreMap:{}", studentScoreMap.containsKey("lcyan"));
		logger.info("StudentScoreMap:{}", studentScoreMap.containsKey("jerry"));
		logger.info("StudentScoreMap:{}", studentScoreMap.size());
		logger.info("StudentScoreMap:{}", studentScoreMap.get("lcyan").size());

		List<StudentScore> StudentScoreList = studentScoreMap.get("lcyan");
		if (StudentScoreList != null && StudentScoreList.size() > 0)
		{
			for (StudentScore stuScore : StudentScoreList)
			{
				logger.info("stuScore one: {}, score: {}", stuScore.courseId, stuScore.score);
			}
		}
	}

	private void addStudentScore(String studentName, StudentScore studentScore)
	{
		List<StudentScore> stuScore = studentScoreMap.get(studentName);

		if (null == stuScore)
		{
			stuScore = Lists.newArrayList();
			studentScoreMap.put(studentName, stuScore);
		}
		stuScore.add(studentScore);
	}

	@Test
	public void testStuScoreMultimap()
	{
		Multimap<String, StudentScore> scoreMultimap = ArrayListMultimap.create();
		for (int i = 10; i < 20; i++)
		{
			StudentScore studentScore = new StudentScore();
			studentScore.courseId = 1001 + i;
			studentScore.score = 100 - i;
			scoreMultimap.put("lcyan", studentScore);
		}
		logger.info("scoreMultimap:{}", scoreMultimap.size());
		logger.info("scoreMultimap:{}", scoreMultimap.keys());
		logger.info("scoreMultimap:{}", scoreMultimap.values());
		logger.info("scoreMultimap:{}", scoreMultimap.get("sss"));
	}
}

class StudentScore
{
	int courseId;
	int score;
}