package in.clouthink.synergy.team.rest.support.impl;

import in.clouthink.synergy.account.domain.model.User;
import in.clouthink.synergy.shared.domain.request.PageSearchRequest;
import in.clouthink.synergy.shared.domain.request.impl.PageSearchParam;
import in.clouthink.synergy.team.domain.request.TaskSearchRequest;
import in.clouthink.synergy.team.rest.param.TaskSearchParam;
import in.clouthink.synergy.team.rest.view.*;
import in.clouthink.synergy.team.rest.support.TaskRestSupport;
import in.clouthink.synergy.team.service.TaskService;
import in.clouthink.synergy.team.service.ActivityService;
import in.clouthink.synergy.team.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Component
public class TaskRestSupportImpl implements TaskRestSupport {

	@Autowired
	private TaskService taskService;

	@Autowired
	private ActivityService activityService;

	@Override
	public Page<TaskView> listTasksByTitle(String title, PageSearchRequest queryRequest, User user) {
		TaskSearchParam messageQueryParameter = new TaskSearchParam();
		messageQueryParameter.setTitle(title);
		messageQueryParameter.setStart(queryRequest.getStart());
		messageQueryParameter.setLimit(queryRequest.getLimit());

		Page<Task> messagePage = taskService.listTasks(messageQueryParameter, null, user);

		return new PageImpl<>(messagePage.getContent()
										 .stream()
										 .map(message -> convertToTaskSummary(user, message))
										 .collect(Collectors.toList()),
							  new PageRequest(queryRequest.getStart(), queryRequest.getLimit()),
							  messagePage.getTotalElements());
	}

	@Override
	public Page<TaskView> listTasksByActivityCreator(String creatorName,
													 PageSearchRequest queryRequest,
													 User user) {
		Page<Task> messagePage = taskService.listTasksByActivityCreator(creatorName, queryRequest, user);
		return new PageImpl<>(messagePage.getContent()
										 .stream()
										 .map(message -> convertToTaskSummary(user, message))
										 .collect(Collectors.toList()),
							  new PageRequest(queryRequest.getStart(), queryRequest.getLimit()),
							  messagePage.getTotalElements());
	}

	@Override
	public Page<TaskView> listTasksByReceiver(String receiverName, PageSearchRequest queryRequest, User user) {
		Page<Task> messagePage = taskService.listTasksByReceiver(receiverName, queryRequest, user);
		return new PageImpl<>(messagePage.getContent()
										 .stream()
										 .map(message -> convertToTaskSummary(user, message))
										 .collect(Collectors.toList()),
							  new PageRequest(queryRequest.getStart(), queryRequest.getLimit()),
							  messagePage.getTotalElements());
	}

	@Override
	public Page<TaskView> listAllTasks(TaskSearchParam queryRequest, User user) {
		queryRequest.setTaskStatus(null);
		Page<Task> messagePage = taskService.listTasks(queryRequest,
													   TaskSearchRequest.IncludeOrExcludeStatus.INCLUDE,
													   user);
		return new PageImpl<>(messagePage.getContent()
										 .stream()
										 .map(message -> convertToTaskSummary(user, message))
										 .collect(Collectors.toList()),
							  new PageRequest(queryRequest.getStart(), queryRequest.getLimit()),
							  messagePage.getTotalElements());
	}

	@Override
	public Page<TaskView> listPendingTasks(TaskSearchParam queryRequest, User user) {
		queryRequest.setTaskStatus(TaskStatus.PENDING);
		Page<Task> messagePage = taskService.listTasks(queryRequest,
													   TaskSearchRequest.IncludeOrExcludeStatus.INCLUDE,
													   user);
		return new PageImpl<>(messagePage.getContent()
										 .stream()
										 .map(message -> convertToTaskSummary(user, message))
										 .collect(Collectors.toList()),
							  new PageRequest(queryRequest.getStart(), queryRequest.getLimit()),
							  messagePage.getTotalElements());
	}

	@Override
	public Page<TaskView> listProcessedTasks(TaskSearchParam queryRequest, User user) {
		queryRequest.setTaskStatus(TaskStatus.PROCESSED);
		Page<Task> messagePage = taskService.listTasks(queryRequest,
													   TaskSearchRequest.IncludeOrExcludeStatus.INCLUDE,
													   user);
		return new PageImpl<>(messagePage.getContent()
										 .stream()
										 .map(message -> convertToTaskSummary(user, message))
										 .collect(Collectors.toList()),
							  new PageRequest(queryRequest.getStart(), queryRequest.getLimit()),
							  messagePage.getTotalElements());
	}

	@Override
	public Page<TaskView> listNotEndTasks(TaskSearchParam queryRequest, User user) {
		queryRequest.setTaskStatus(TaskStatus.ENDED);
		Page<Task> messagePage = taskService.listTasks(queryRequest,
													   TaskSearchRequest.IncludeOrExcludeStatus.EXCLUDE,
													   user);
		return new PageImpl<>(messagePage.getContent()
										 .stream()
										 .map(message -> convertToTaskSummary(user, message))
										 .collect(Collectors.toList()),
							  new PageRequest(queryRequest.getStart(), queryRequest.getLimit()),
							  messagePage.getTotalElements());
	}

	@Override
	public Page<TaskView> listEndedTasks(TaskSearchParam queryRequest, User user) {
		queryRequest.setTaskStatus(TaskStatus.ENDED);
		Page<Task> messagePage = taskService.listTasks(queryRequest,
													   TaskSearchRequest.IncludeOrExcludeStatus.INCLUDE,
													   user);
		return new PageImpl<>(messagePage.getContent()
										 .stream()
										 .map(message -> convertToTaskSummary(user, message))
										 .collect(Collectors.toList()),
							  new PageRequest(queryRequest.getStart(), queryRequest.getLimit()),
							  messagePage.getTotalElements());
	}

	@Override
	public Page<TaskView> listFavoriteTasks(TaskSearchParam queryRequest, User user) {
		Page<Task> messagePage = taskService.listFavoriteTasks(queryRequest, user);
		return new PageImpl<>(messagePage.getContent().stream().map(message -> {
			Activity activity = activityService.findActivityById(message.getBizRefId());
			TaskView result = TaskView.from(message, activity);
			result.setRead(activityService.isRead(activity, user));
			result.setFavorite(true);
			return result;
		}).collect(Collectors.toList()),
							  new PageRequest(queryRequest.getStart(), queryRequest.getLimit()),
							  messagePage.getTotalElements());
	}

	@Override
	public TaskDetailView getTaskDetail(String id, User user) {
		Task task = taskService.findTaskById(id, user);
		if (task == null) {
			return null;
		}
		Activity activity = activityService.findActivityById(task.getBizRefId(), user);
		TaskDetailView result = TaskDetailView.from(task, activity);
		result.setRead(activityService.isRead(activity, user));
		result.setFavorite(taskService.isFavorite(task, user));
		return result;
	}

	@Override
	public TaskParticipantView getTaskParticipant(String id, User user) {
		Task task = taskService.findTaskById(id, user);
		if (task == null) {
			return null;
		}
		String actionRefId = task.getActionRefId();
		if (StringUtils.isEmpty(actionRefId)) {
			return null;
		}
		ActivityAction activityAction = activityService.findActivityActionById(actionRefId);
		if (activityAction == null) {
			return null;
		}

		return TaskParticipantView.from(task.getSender(), user, activityAction);
	}

	@Override
	public long getCountOfAllTasks(TaskSearchParam queryRequest, User user) {
		queryRequest.setTaskStatus(null);
		return taskService.countOfTasks(queryRequest, TaskSearchRequest.IncludeOrExcludeStatus.INCLUDE, user);
	}

	@Override
	public long getCountOfPendingTasks(TaskSearchParam queryRequest, User user) {
		queryRequest.setTaskStatus(TaskStatus.PENDING);
		return taskService.countOfTasks(queryRequest, TaskSearchRequest.IncludeOrExcludeStatus.INCLUDE, user);
	}

	@Override
	public long getCountOfProcessedTasks(TaskSearchParam queryRequest, User user) {
		queryRequest.setTaskStatus(TaskStatus.PROCESSED);
		return taskService.countOfTasks(queryRequest, TaskSearchRequest.IncludeOrExcludeStatus.INCLUDE, user);
	}

	@Override
	public long getCountOfEndedTasks(TaskSearchParam queryRequest, User user) {
		queryRequest.setTaskStatus(TaskStatus.ENDED);
		return taskService.countOfTasks(queryRequest, TaskSearchRequest.IncludeOrExcludeStatus.INCLUDE, user);
	}

	@Override
	public long getCountOfNotEndTasks(TaskSearchParam queryRequest, User user) {
		queryRequest.setTaskStatus(TaskStatus.ENDED);
		return taskService.countOfTasks(queryRequest, TaskSearchRequest.IncludeOrExcludeStatus.EXCLUDE, user);
	}

	@Override
	public long getCountOfFavoriteTasks(PageSearchParam queryRequest, User user) {
		return taskService.countOfFavoriteTasks(queryRequest, user);
	}

	@Override
	public void addTaskToFavorite(String id, User user) {
		taskService.addTaskToFavorite(id, user);
	}

	@Override
	public void removeTaskFromFavorite(String id, User user) {
		taskService.removeTaskFromFavorite(id, user);
	}


	private TaskView convertToTaskSummary(User user, Task task) {
		Activity activity = activityService.findActivityById(task.getBizRefId(), user);
		TaskView result = TaskView.from(task, activity);
		result.setFavorite(taskService.isFavorite(task, user));
		return result;
	}

}
