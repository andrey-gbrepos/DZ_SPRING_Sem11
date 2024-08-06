package ru.gb.timesheet.page;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
////import ru.gb.timesheet.model.Project;
//import ru.gb.timesheet.service.ProjectPageService;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/home/projects")
//@RequiredArgsConstructor
//public class ProjectPageController {
//
//    private final ProjectPageService service;
//
//    @GetMapping
//    public String getAllProjects(Model model) {
//        List<Project> projects = service.findAll();
//        model.addAttribute("projects", projects);
//        return "project-page.html";
//    }
//
//    @GetMapping("/{id}")
//    private String getTimesheetPage(@PathVariable Long id, Model model) {
//        List<Project> projects = new ArrayList<>();
//        Optional<Project> projectOpt = service.findById(id);
//        if (projectOpt.isEmpty()) {
//            return "not-found.html";
//        }
//        projects.add(projectOpt.get());
//        model.addAttribute("projects", projects);
//        return "project-page.html";
//    }
//}