package io.pivotal.pal.tracker.timesheets;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestOperations;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


public class ProjectClient {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final Map<Long, ProjectInfo> projectsCache = new ConcurrentHashMap<>();
	
    private final RestOperations restOperations;
    private final String endpoint;

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }

    public ProjectInfo getProject(long projectId) {
        return restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
    }
    
    @HystrixCommand(fallbackMethod = "getProjectFromCache")
	public ProjectInfo getProjectFromCache(long projectId) {
		logger.info("Getting project with id {} from cache", projectId);
		return projectsCache.get(projectId);
	}
}
